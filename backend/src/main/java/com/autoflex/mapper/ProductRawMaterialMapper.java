package com.autoflex.mapper;

import com.autoflex.dto.ProductRawMaterialResponseDTO;
import com.autoflex.entity.ProductRawMaterial;

public class ProductRawMaterialMapper {

    public static ProductRawMaterialResponseDTO toResponse(ProductRawMaterial prm) {
        ProductRawMaterialResponseDTO dto = new ProductRawMaterialResponseDTO();
        dto.id = prm.id;
        dto.productId = prm.product.id;
        dto.rawMaterialId = prm.rawMaterial.id;
        dto.quantityRequired = prm.quantityRequired;
        return dto;
    }
}
