describe("Raw Materials E2E", () => {
  const rawName = `RawMat-${Date.now()}`;
  const rawQuantity = 100;
  let rawId: string;

  it("should list raw materials from API", () => {
    cy.request("GET", "/raw-materials").then((res) => {
      const raws = res.body.data ?? [];
      cy.visit("/raw-materials");
      raws.forEach((r) => {
        cy.contains(r.name);
        cy.contains(r.id);
        cy.contains(r.quantity);
      });
    });
  });

  it("should navigate to create page", () => {
    cy.visit("/raw-materials");
    cy.get('[data-cy=create-raw]', { timeout: 10000 }).should("be.visible").click();
    cy.url().should("include", "/raw-materials/new");
  });

  it("should create a new raw material", () => {
    cy.visit("/raw-materials/new");

    cy.get('[data-cy="raw-name"]').scrollIntoView().type(rawName);
    cy.get('[data-cy="raw-quantity"]').scrollIntoView().type(`${rawQuantity}`);
    cy.get('[data-cy="submit-raw"]').scrollIntoView().click();

    cy.contains('Create Raw Material').should("be.visible");

    cy.visit("/raw-materials");
    cy.contains(rawName).should("exist");

    cy.get(`[data-cy^="raw-row-"]`)
      .contains(rawName)
      .closest("tr")
      .invoke("attr", "data-cy")
      .then((idAttr) => {
        rawId = idAttr!.replace("raw-row-", "");
      });
  });

  it("should edit the raw material", () => {
    cy.visit("/raw-materials");
    cy.get(`[data-cy=edit-raw-${rawId}]`).scrollIntoView().click();

    cy.url().should("include", `/raw-materials/${rawId}/edit`);

    const newQuantity = rawQuantity + 50;
    cy.get('[data-cy="raw-quantity"]').clear().type(`${newQuantity}`);
    cy.get('[data-cy="submit-raw"]').click();
    cy.visit("/raw-materials");
    cy.get(`[data-cy=raw-row-${rawId}]`).contains(newQuantity);
  });

  it("should delete the raw material", () => {
    cy.visit("/raw-materials");
    cy.get(`[data-cy=delete-raw-${rawId}]`).scrollIntoView().click();

    cy.get('[data-cy=delete-raw-modal]')
      .should("be.visible")
      .within(() => {
        cy.get('[data-cy=confirm-delete]').click();
      });

    cy.contains('Raw material deleted successfully!').should("be.visible");    
    cy.contains(rawName).should("not.exist");
  });
});