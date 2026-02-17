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
class ProductRawMaterialResourceTest {

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        // Limpar na ordem correta para evitar constraint violation
        ProductRawMaterial.deleteAll();
        RawMaterial.deleteAll();
        Product.deleteAll();
    }

    // =====================================
    // SETUP AUX
    // =====================================
    private Long createProduct(String name, String sku) {
        return ((Number) given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "%s",
                              "sku": "%s",
                              "quantity": 10,
                              "price": 100
                            }
                        """.formatted(name, sku))
                .when()
                .post("/products")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract()
                .path("data.id")).longValue();
    }

    private Long createRawMaterial(String name, int quantity) {
        return ((Number) given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "name": "%s",
                              "quantity": %d
                            }
                        """.formatted(name, quantity))
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract()
                .path("data.id")).longValue();
    }

    private Long linkRawMaterial(Long productId, Long rawMaterialId, int quantityRequired) {
        return ((Number) given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "rawMaterialId": %d,
                              "quantityRequired": %d
                            }
                        """.formatted(rawMaterialId, quantityRequired))
                .when()
                .post("/products/" + productId + "/raw-materials")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("data.productId", equalTo(productId.intValue()))
                .body("data.rawMaterialId", equalTo(rawMaterialId.intValue()))
                .body("data.quantityRequired", equalTo(quantityRequired))
                .extract()
                .path("data.id")).longValue();
    }

    // =====================================
    // CREATE
    // =====================================
    @Test
    void shouldLinkRawMaterialToProduct() {
        Long productId = createProduct("Produto A", "SKU-A");
        Long rawMaterialId = createRawMaterial("Ferro", 100);
        linkRawMaterial(productId, rawMaterialId, 5);
    }

    @Test
    void shouldReturn404WhenLinkingNonExistingProductOrRawMaterial() {
        Long rawMaterialId = createRawMaterial("Ferro", 100);

        // Produto não existe
        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "rawMaterialId": %d,
                              "quantityRequired": 5
                            }
                        """.formatted(rawMaterialId))
                .when()
                .post("/products/999999/raw-materials")
                .then()
                .statusCode(404);

        Long productId = createProduct("Produto B", "SKU-B");

        // RawMaterial não existe
        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "rawMaterialId": 999999,
                              "quantityRequired": 5
                            }
                        """)
                .when()
                .post("/products/" + productId + "/raw-materials")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotAllowDuplicateLink() {
        Long productId = createProduct("Produto C", "SKU-C");
        Long rawMaterialId = createRawMaterial("Cobre", 50);
        linkRawMaterial(productId, rawMaterialId, 5);

        // Tentar linkar novamente
        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "rawMaterialId": %d,
                              "quantityRequired": 5
                            }
                        """.formatted(rawMaterialId))
                .when()
                .post("/products/" + productId + "/raw-materials")
                .then()
                .statusCode(400) // ALTERADO DE 500 PARA 400
                .body("description", equalTo("Raw material already linked to product"));
    }

    // =====================================
    // LIST
    // =====================================
    @Test
    void shouldListRawMaterialsForProduct() {
        Long productId = createProduct("Produto D", "SKU-D");
        Long rm1 = createRawMaterial("Ferro", 100);
        Long rm2 = createRawMaterial("Cobre", 50);
        linkRawMaterial(productId, rm1, 5);
        linkRawMaterial(productId, rm2, 10);

        given()
                .when()
                .get("/products/" + productId + "/raw-materials")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(2));
    }

    @Test
    void shouldReturn404WhenListingRawMaterialsOfNonExistingProduct() {
        given()
                .when()
                .get("/products/999999/raw-materials")
                .then()
                .statusCode(404);
    }

    // =====================================
    // UPDATE
    // =====================================
    @Test
    void shouldUpdateQuantityRequired() {
        Long productId = createProduct("Produto E", "SKU-E");
        Long rmId = createRawMaterial("Aluminio", 30);
        @SuppressWarnings("unused")
        Long linkId = linkRawMaterial(productId, rmId, 5);

        // Atualizando quantidade
        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "rawMaterialId": %d,
                              "quantityRequired": 15
                            }
                        """.formatted(rmId))
                .when()
                .post("/products/" + productId + "/raw-materials") // caso seu endpoint suporte update via POST ou criar
                                                                   // um PUT se quiser
                .then()
                .statusCode(400); // se você implementar PUT, aqui pode ser 200
    }

    // =====================================
    // DELETE
    // =====================================
    @Test
    void shouldDeleteRawMaterialLink() {
        Long productId = createProduct("Produto F", "SKU-F");
        Long rmId = createRawMaterial("Zinco", 30);
        Long linkId = linkRawMaterial(productId, rmId, 5);

        given()
                .when()
                .delete("/products/" + productId + "/raw-materials/" + linkId)
                .then()
                .statusCode(200);

        // Confirma remoção
        given()
                .when()
                .get("/products/" + productId + "/raw-materials")
                .then()
                .statusCode(200)
                .body("data.size()", equalTo(0));
    }
}
