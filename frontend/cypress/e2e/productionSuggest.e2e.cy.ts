describe('Production Suggestion - Real Data Validation', () => {

  beforeEach(() => {
    // Visita a página de Production Suggestion
    cy.visit('http://localhost:5173/production-suggestion')
  })

  it('should display all producible products correctly with totals', () => {
    // Busca os dados reais da API
    cy.request('GET', 'http://localhost:8080/products/production-suggestions')
      .then((res) => {
        const data = res.body as ProductionPlanResponseDTO
        const products = data.products
        const grandTotal = data.grandTotal

        // Se não houver produtos, verifica mensagem de empty state
        if (products.length === 0) {
          cy.contains('No products can be produced with current stock').should('be.visible')
          cy.contains('Total Production Value').should('contain', `$0`)
          return
        }

        // Valida cada produto na tabela
        products.forEach((prod) => {
          cy.get('table tbody tr')
            .contains(prod.name)
            .parent('tr')
            .within(() => {
              cy.get('td').eq(0).should('contain', prod.name)
              cy.get('td').eq(1).should('contain', prod.sku)
              cy.get('td').eq(2).should('contain', prod.quantityProducible)
              cy.get('td').eq(3).should('contain', `$${prod.price}`)
              cy.get('td').eq(4).should('contain', `$${prod.totalValue}`)
            })
        })

        // Valida o grandTotal exibido na UI
        cy.contains('Total Production Value').should('contain', `$${grandTotal}`)
      })
  })

  it('should show loading state while fetching data', () => {
    // Intercepta e adiciona delay para simular carregamento
    cy.intercept('GET', 'http://localhost:8080/products/production-suggestions', (req) => {
      req.on('response', (res) => res.setDelay(1000))
    }).as('getProduction')

    cy.visit('http://localhost:5173/production-suggestion')

    // Loading deve aparecer
    cy.contains('Loading production plan...').should('be.visible')

    // Aguarda resposta
    cy.wait('@getProduction')

    // Loading desaparece
    cy.contains('Loading production plan...').should('not.exist')
  })
})