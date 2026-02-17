package com.autoflex.service;

import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.entity.Product;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ProductServiceTest {

    @Inject
    ProductService service;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Product.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateProductSuccessfully() {

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.name = "Pneu 195/55R15";
        dto.sku = "PN1955515";
        dto.quantity = 10;
        dto.price = 299.90;

        ProductResponseDTO response = service.create(dto);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id);
        Assertions.assertEquals("Pneu 195/55R15", response.name);
        Assertions.assertEquals("PN1955515", response.sku);
        Assertions.assertEquals(10, response.quantity);
        Assertions.assertEquals(299.90, response.price);
        Assertions.assertNotNull(response.createdAt);
        Assertions.assertNotNull(response.updatedAt);
    }

    @Test
    @Transactional
    void shouldNotAllowDuplicateSku() {

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.name = "Produto 1";
        dto.sku = "SKU123";
        dto.quantity = 5;
        dto.price = 100.0;

        service.create(dto);

        ProductRequestDTO duplicate = new ProductRequestDTO();
        duplicate.name = "Produto 2";
        duplicate.sku = "SKU123";
        duplicate.quantity = 3;
        duplicate.price = 200.0;

        Assertions.assertThrows(
                jakarta.ws.rs.BadRequestException.class,
                () -> service.create(duplicate)
        );
    }

    @Test
@Transactional
void shouldUpdateProductKeepingSameSku() {

    ProductRequestDTO dto = new ProductRequestDTO();
    dto.name = "Produto Original";
    dto.sku = "SKU-UPDATE";
    dto.quantity = 10;
    dto.price = 100.0;

    ProductResponseDTO created = service.create(dto);

    ProductRequestDTO updateDto = new ProductRequestDTO();
    updateDto.name = "Produto Atualizado";
    updateDto.sku = "SKU-UPDATE"; // mesmo SKU
    updateDto.quantity = 20;
    updateDto.price = 200.0;

    ProductResponseDTO updated = service.update(created.id, updateDto);

    Assertions.assertEquals("Produto Atualizado", updated.name);
    Assertions.assertEquals(20, updated.quantity);
    Assertions.assertEquals(200.0, updated.price);
}

}
