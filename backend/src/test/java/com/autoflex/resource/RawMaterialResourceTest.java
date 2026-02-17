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
class RawMaterialResourceTest {

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
    void shouldCreateRawMaterial() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Aço",
                              "quantity": 100
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("data.id", notNullValue())
                .body("data.name", equalTo("Aço"))
                .body("data.quantity", equalTo(100));
    }

    @Test
    void shouldReturn400WhenNameAlreadyExists() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Plástico",
                              "quantity": 50
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(anyOf(is(200), is(201)));

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Plástico",
                              "quantity": 30
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(400)
                .body("description", equalTo("Raw material already exists"));
    }

    // =====================================
    // UPDATE
    // =====================================

    @Test
    void shouldUpdateRawMaterialSuccessfully() {

        String id = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Alumínio",
                              "quantity": 80
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Alumínio Atualizado",
                              "quantity": 120
                            }
                        """)
                .when()
                .put("/raw-materials/" + id)
                .then()
                .statusCode(200)
                .body("data.name", equalTo("Alumínio Atualizado"))
                .body("data.quantity", equalTo(120));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingRawMaterial() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Inexistente",
                              "quantity": 10
                            }
                        """)
                .when()
                .put("/raw-materials/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn400WhenUpdatingWithDuplicateName() {

        @SuppressWarnings("unused")
        String id1 = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Cobre",
                              "quantity": 40
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .extract()
                .path("data.id")
                .toString();

        String id2 = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Ferro",
                              "quantity": 60
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Cobre",
                              "quantity": 60
                            }
                        """)
                .when()
                .put("/raw-materials/" + id2)
                .then()
                .statusCode(400)
                .body("description", equalTo("Raw material already exists"));
    }

    // =====================================
    // DELETE
    // =====================================

    @Test
    void shouldDeleteRawMaterialSuccessfully() {

        String id = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Vidro",
                              "quantity": 25
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .when()
                .delete("/raw-materials/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/raw-materials/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingRawMaterial() {

        given()
                .when()
                .delete("/raw-materials/999999")
                .then()
                .statusCode(404);
    }

    // =====================================
    // GET
    // =====================================

    @Test
    void shouldFindRawMaterialById() {

        String id = given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Borracha",
                              "quantity": 70
                            }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .extract()
                .path("data.id")
                .toString();

        given()
                .when()
                .get("/raw-materials/" + id)
                .then()
                .statusCode(200)
                .body("data.name", equalTo("Borracha"))
                .body("data.quantity", equalTo(70));
    }

    @Test
    void shouldListAllRawMaterials() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Material 1",
                              "quantity": 10
                            }
                        """)
                .when()
                .post("/raw-materials");

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "Material 2",
                              "quantity": 20
                            }
                        """)
                .when()
                .post("/raw-materials");

        given()
                .when()
                .get("/raw-materials")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(2));
    }
}
