describe('Products E2E', () => {
    const productSKU = `SKU-${Date.now()}`;
  const productName = `Test Product ${Date.now()}`;

  it('should list products from API', () => {
    cy.request('GET', '/products').then((res) => {
      const products = res.body.data ?? []
      cy.visit('/products')
      products.forEach(p => {
        cy.contains(p.name)
        cy.contains(p.sku)
        cy.contains(p.price.toLocaleString('en-US', { style: 'currency', currency: 'USD' }))
        cy.contains(p.quantity)
      })
    })
  })

  it('should navigate to create page', () => {
    cy.visit('/products')
    cy.get('[data-cy=create-product]').click()
    cy.url().should('include', '/products/new')
  })

  it('should edit a product', () => {
    cy.visit('/products')
    cy.get('[data-cy^=edit-product-]').first().click()
    cy.url().should('match', /\/products\/\d+\/edit/) 
    cy.get('[data-cy=product-name]').clear().type('Updated Name')
    cy.get('[data-cy=update-product]').click()
    cy.contains('Product updated successfully!')
  })

  it('should delete a product', () => {
    cy.visit('/products')
cy.get('[data-cy^=delete-product-]').first().click() // abre modal
cy.wait(500)
cy.get('[data-cy=delete-product-modal]') // busca modal
  .should('be.visible') // espera ele ficar visível
  .within(() => {
    cy.get('[data-cy=confirm-delete]').click() // clica Delete
  })

cy.contains('Product deleted successfully!').should('be.visible')
  })

  it('should create a new product with materials', () => {
    cy.visit("/products");
    cy.get('[data-cy=create-product]').click();
    cy.get('[data-cy="product-sku"]').scrollIntoView().type(productSKU);
    cy.get('[data-cy="product-name"]').type(productName);
    cy.get('[data-cy="product-price"]').scrollIntoView().clear().type("50");
    cy.get('[data-cy="product-quantity"]').type("10");
    cy.get('button').contains('+ Add Material').scrollIntoView().click();
    cy.get('[data-cy="materials-modal"]').should('be.visible').within(() => {
      cy.get('input').first().type("5");
      cy.get('button').contains('Add Selected').click();
    });
    cy.contains('Raw Materials Composition')
      .parent()
      .within(() => {
        cy.get('tbody tr').should('have.length.greaterThan', 0);
      });
    cy.get('button').contains('Create Product').scrollIntoView().click();
    cy.contains(`Product "${productName}" created successfully!`).should('be.visible');
    cy.visit("/products");
    cy.contains(productSKU).should('exist');
    cy.contains(productName).should('exist');
  })

})