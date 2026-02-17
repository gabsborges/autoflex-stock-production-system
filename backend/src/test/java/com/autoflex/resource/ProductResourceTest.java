package com.autoflex.resource;

import com.autoflex.entity.Product;
import com.autoflex.entity.ProductRawMaterial;
import com.autoflex.entity.RawMaterial;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductResourceTest {

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        ProductRawMaterial.deleteAll();
        Product.deleteAll();
        RawMaterial.deleteAll();
    }

    // =====================================
    // CREATE
    // =====================================

    @Test
    void shouldCreateProductViaEndpoint() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Pneu 205/55R16",
                              "sku": "PN2055516",
                              "quantity": 20,
                              "price": 399.90
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("data.id", notNullValue())
                .body("data.name", equalTo("Pneu 205/55R16"))
                .body("data.sku", equalTo("PN2055516"));
    }

    @Test
    void shouldReturn400WhenSkuAlreadyExists() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto X",
                              "sku": "DUPLICATE",
                              "quantity": 5,
                              "price": 100.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto Y",
                              "sku": "DUPLICATE",
                              "quantity": 3,
                              "price": 50.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .statusCode(400)
                .body("description", equalTo("SKU already exists"));
    }

    // =====================================
    // UPDATE
    // =====================================

    @Test
    void shouldUpdateProductSuccessfully() {

        String id = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto A",
                              "sku": "SKU-A",
                              "quantity": 10,
                              "price": 100.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto Atualizado",
                              "sku": "SKU-A",
                              "quantity": 20,
                              "price": 200.0
                            }
                        """)
                .when()
                .put("/products/" + id)
                .then()
                .statusCode(200)
                .body("data.name", equalTo("Produto Atualizado"))
                .body("data.quantity", equalTo(20));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingProduct() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto X",
                              "sku": "SKU-X",
                              "quantity": 10,
                              "price": 100.0
                            }
                        """)
                .when()
                .put("/products/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn400WhenUpdatingWithDuplicateSku() {

        // Criar primeiro produto
        @SuppressWarnings("unused")
        String id1 = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto 1",
                              "sku": "SKU-1",
                              "quantity": 10,
                              "price": 100.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .extract()
                .path("data.id")
                .toString();

        // Criar segundo produto
        String id2 = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto 2",
                              "sku": "SKU-2",
                              "quantity": 5,
                              "price": 50.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .extract()
                .path("data.id")
                .toString();

        // Tentar atualizar o segundo produto com SKU do primeiro
        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto 2 Atualizado",
                              "sku": "SKU-1",
                              "quantity": 5,
                              "price": 50.0
                            }
                        """)
                .when()
                .put("/products/" + id2)
                .then()
                .statusCode(400)
                .body("description", equalTo("SKU already exists"));
    }

    // =====================================
    // DELETE
    // =====================================

    @Test
    void shouldDeleteProductSuccessfully() {

        String id = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto Delete",
                              "sku": "SKU-DEL",
                              "quantity": 10,
                              "price": 100.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(200);

        // Confirmar que foi removido
        given()
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingProduct() {

        given()
                .when()
                .delete("/products/999999")
                .then()
                .statusCode(404);
    }

    // =====================================
    // GET
    // =====================================

    @Test
    void shouldFindProductById() {

        String id = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto GET",
                              "sku": "SKU-GET",
                              "quantity": 15,
                              "price": 150.0
                            }
                        """)
                .when()
                .post("/products")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(200)
                .body("data.name", equalTo("Produto GET"))
                .body("data.sku", equalTo("SKU-GET"));
    }

    @Test
    void shouldListAllProducts() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto 1",
                              "sku": "SKU-L1",
                              "quantity": 5,
                              "price": 50.0
                            }
                        """)
                .when()
                .post("/products");

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Produto 2",
                              "sku": "SKU-L2",
                              "quantity": 10,
                              "price": 100.0
                            }
                        """)
                .when()
                .post("/products");

        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(2));
    }

    @Test
    void shouldReturnPagedProducts() {

        // Criar 5 produtos
        for (int i = 1; i <= 5; i++) {
            given()
                    .contentType(ContentType.JSON)
                    .body("""
                                {
                                  "name": "Produto %d",
                                  "sku": "SKU-P%d",
                                  "quantity": 10,
                                  "price": 100.0
                                }
                            """.formatted(i, i))
                    .when()
                    .post("/products");
        }

        // Buscar página 0 com tamanho 2
        given()
                .when()
                .get("/products/paged?page=0&size=2&sortBy=name&direction=asc")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(2));
    }

    @Test
    void shouldReturnProductsOrderedByNameAsc() {

        given().contentType(ContentType.JSON).body("""
                    {
                      "name": "Produto C",
                      "sku": "SKU-C",
                      "quantity": 5,
                      "price": 50.0
                    }
                """).post("/products");

        given().contentType(ContentType.JSON).body("""
                    {
                      "name": "Produto A",
                      "sku": "SKU-A1",
                      "quantity": 5,
                      "price": 50.0
                    }
                """).post("/products");

        given().contentType(ContentType.JSON).body("""
                    {
                      "name": "Produto B",
                      "sku": "SKU-B",
                      "quantity": 5,
                      "price": 50.0
                    }
                """).post("/products");

        given()
                .when()
                .get("/products/paged?page=0&size=10&sortBy=name&direction=asc")
                .then()
                .statusCode(200)
                .body("data[0].name", equalTo("Produto A"))
                .body("data[1].name", equalTo("Produto B"))
                .body("data[2].name", equalTo("Produto C"));
    }

    @Test
    void shouldReturnProductsOrderedByNameDesc() {

        given().contentType(ContentType.JSON).body("""
                    {
                      "name": "Produto A",
                      "sku": "SKU-1",
                      "quantity": 5,
                      "price": 50.0
                    }
                """).post("/products");

        given().contentType(ContentType.JSON).body("""
                    {
                      "name": "Produto B",
                      "sku": "SKU-2",
                      "quantity": 5,
                      "price": 50.0
                    }
                """).post("/products");

        given()
                .when()
                .get("/products/paged?page=0&size=10&sortBy=name&direction=desc")
                .then()
                .statusCode(200)
                .body("data[0].name", equalTo("Produto B"))
                .body("data[1].name", equalTo("Produto A"));
    }

    @Test
    void shouldReturn400WhenDirectionIsInvalid() {

        given()
                .when()
                .get("/products/paged?page=0&size=10&sortBy=name&direction=invalid")
                .then()
                .statusCode(400);
    }
}
