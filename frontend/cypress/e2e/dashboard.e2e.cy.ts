describe('Dashboard - Real Data Validation', () => {

  it('should display correct totals based on database data', () => {

    // Buscar produtos reais
    cy.request('GET', 'http://localhost:8080/products')
      .then((productsResponse) => {

        const products = productsResponse.body.data ?? []
        const totalProducts = products.length

        // Buscar matérias-primas reais
        cy.request('GET', 'http://localhost:8080/raw-materials')
          .then((rawMaterialsResponse) => {

            const rawMaterials = rawMaterialsResponse.body.data ?? []
            const totalRawMaterials = rawMaterials.length

            const totalStockItems = rawMaterials.reduce(
              (acc: number, item: any) => acc + (item.quantity ?? 0),
              0
            )

            // Agora visita o front
            cy.visit('http://localhost:5173')

            // Valida UI vs API
            cy.contains('Total Products')
              .parent()
              .should('contain', totalProducts)

            cy.contains('Total Raw Materials')
              .parent()
              .should('contain', totalRawMaterials)

            cy.contains('Total Stock Items')
              .parent()
              .should('contain', totalStockItems.toLocaleString())
          })
      })
  })


  describe('Dashboard - Loading State', () => {

  it('should show loading while fetching data', () => {

    cy.intercept('GET', 'http://localhost:8080/products', (req) => {
      req.on('response', (res) => {
        res.setDelay(1000)
      })
    }).as('getProducts')

    cy.intercept('GET', 'http://localhost:8080/raw-materials', (req) => {
      req.on('response', (res) => {
        res.setDelay(1000)
      })
    }).as('getRawMaterials')

    cy.visit('/')

    cy.contains('Loading...').should('be.visible')

    cy.wait(['@getProducts', '@getRawMaterials'])

    cy.contains('Loading...').should('not.exist')
  })
})

describe('Dashboard - Quick Actions', () => {

  beforeEach(() => {
    cy.visit('/')
  })

  it('should navigate to New Product page', () => {
    cy.contains('New Product').click()
    cy.url().should('include', '/products/new')
  })

  it('should navigate to New Raw Material page', () => {
    cy.contains('New Raw Material').click()
    cy.url().should('include', '/raw-materials/new')
  })

  it('should navigate to Production Suggestion page', () => {
    cy.contains('Production suggestion').click()
    cy.url().should('include', '/production-suggestion')
  })
})

})