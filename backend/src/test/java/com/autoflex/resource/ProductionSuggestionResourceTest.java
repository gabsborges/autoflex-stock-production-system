package com.autoflex.resource;

import com.autoflex.dto.ProductRawMaterialRequestDTO;
import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.RawMaterialRequestDTO;
import com.autoflex.entity.Product;
import com.autoflex.entity.ProductRawMaterial;
import com.autoflex.entity.RawMaterial;
import com.autoflex.service.ProductRawMaterialService;
import com.autoflex.service.ProductService;
import com.autoflex.service.RawMaterialService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ProductionSuggestionResourceTest {

    @Inject
    RawMaterialService rawMaterialService;

    @Inject
    ProductService productService;

    @Inject
    ProductRawMaterialService productRawMaterialService;

    @BeforeEach
    @Transactional
    public void setup() {

        ProductRawMaterial.deleteAll();
        Product.deleteAll();
        RawMaterial.deleteAll();

        // -------- RAW MATERIALS --------
        RawMaterialRequestDTO steel = new RawMaterialRequestDTO();
        steel.name = "Steel";
        steel.quantity = 100;
        rawMaterialService.create(steel);

        RawMaterialRequestDTO plastic = new RawMaterialRequestDTO();
        plastic.name = "Plastic";
        plastic.quantity = 50;
        rawMaterialService.create(plastic);

        RawMaterial steelEntity = RawMaterial.find("name", "Steel").firstResult();
        RawMaterial plasticEntity = RawMaterial.find("name", "Plastic").firstResult();

        // -------- PRODUCTS --------
        ProductRequestDTO car = new ProductRequestDTO();
        car.name = "Car";
        car.sku = "CAR001";
        car.price = 1000.0;
        car.quantity = 1; // precisa ser > 0
        productService.create(car);

        ProductRequestDTO toy = new ProductRequestDTO();
        toy.name = "Toy";
        toy.sku = "TOY001";
        toy.price = 200.0;
        toy.quantity = 1; // precisa ser > 0
        productService.create(toy);

        Product carEntity = Product.find("sku", "CAR001").firstResult();
        Product toyEntity = Product.find("sku", "TOY001").firstResult();

        // -------- LINKS --------
        ProductRawMaterialRequestDTO carSteel = new ProductRawMaterialRequestDTO();
        carSteel.rawMaterialId = steelEntity.id;
        carSteel.quantityRequired = 20;
        productRawMaterialService.create(carEntity.id, carSteel);

        ProductRawMaterialRequestDTO toySteel = new ProductRawMaterialRequestDTO();
        toySteel.rawMaterialId = steelEntity.id;
        toySteel.quantityRequired = 10;
        productRawMaterialService.create(toyEntity.id, toySteel);

        ProductRawMaterialRequestDTO toyPlastic = new ProductRawMaterialRequestDTO();
        toyPlastic.rawMaterialId = plasticEntity.id;
        toyPlastic.quantityRequired = 5;
        productRawMaterialService.create(toyEntity.id, toyPlastic);
    }

    @Test
    public void testProductionSuggestions() {

        given()
            .when()
            .get("/products/production-suggestions")
            .then()
            .statusCode(200)
            .body("size()", is(2))
            .body("[0].name", equalTo("Car"))
            .body("[0].quantityProducible", equalTo(5)) // 100 / 20
            .body("[1].name", equalTo("Toy"))
            .body("[1].quantityProducible", equalTo(10)); // min(100/10 , 50/5)
    }
}
