package com.autoflex.service;

import java.util.List;
import java.util.stream.Collectors;

import com.autoflex.dto.ProductRawMaterialRequestDTO;
import com.autoflex.dto.ProductRawMaterialResponseDTO;
import com.autoflex.entity.Product;
import com.autoflex.entity.ProductRawMaterial;
import com.autoflex.entity.RawMaterial;
import com.autoflex.mapper.ProductRawMaterialMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ProductRawMaterialService {

    @Transactional
    public ProductRawMaterialResponseDTO create(Long productId, ProductRawMaterialRequestDTO dto) {
        Product product = Product.findById(productId);
        if (product == null)
            throw new NotFoundException("Product not found");

        RawMaterial rawMaterial = RawMaterial.findById(dto.rawMaterialId);
        if (rawMaterial == null)
            throw new NotFoundException("RawMaterial not found");

        ProductRawMaterial existing = ProductRawMaterial
                .find("product = ?1 and rawMaterial = ?2", product, rawMaterial)
                .firstResult();
        if (existing != null) {
            throw new RuntimeException("This raw material is already linked to the product");
        }

        ProductRawMaterial prm = new ProductRawMaterial();
        prm.product = product;
        prm.rawMaterial = rawMaterial;
        prm.quantityRequired = dto.quantityRequired;
        prm.persist();

        return ProductRawMaterialMapper.toResponse(prm);
    }

    public List<ProductRawMaterialResponseDTO> listByProduct(Long productId) {
        Product product = Product.findById(productId);
        if (product == null)
            throw new NotFoundException("Product not found");

        return ProductRawMaterial.find("product", product)
                .list()
                .stream()
                .map(prm -> ProductRawMaterialMapper.toResponse((ProductRawMaterial) prm))
                .collect(Collectors.toList());
    }
}
