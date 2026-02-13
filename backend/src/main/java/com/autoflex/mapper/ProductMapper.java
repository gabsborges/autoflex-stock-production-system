package com.autoflex.mapper;

import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.name = dto.name;
        product.sku = dto.sku;
        product.quantity = dto.quantity;
        product.price = dto.price;
        return product;
    }

    public static ProductResponseDTO toResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.id = product.id;
        dto.name = product.name;
        dto.sku = product.sku;
        dto.quantity = product.quantity;
        dto.price = product.price;
        return dto;
    }
}
