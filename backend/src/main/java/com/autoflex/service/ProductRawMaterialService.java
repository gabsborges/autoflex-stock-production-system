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
import jakarta.ws.rs.BadRequestException;
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
            throw new BadRequestException("Raw material already linked to product");
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

    @Transactional
    public ProductRawMaterialResponseDTO updateQuantity(Long productId, Long id, Integer quantity) {
        ProductRawMaterial prm = ProductRawMaterial.findById(id);
        if (prm == null || !prm.product.id.equals(productId)) {
            throw new NotFoundException("Raw material link not found");
        }

        prm.quantityRequired = quantity;
        prm.persist();
        return ProductRawMaterialMapper.toResponse(prm);
    }

    @Transactional
    public void delete(Long productId, Long id) {
        ProductRawMaterial prm = ProductRawMaterial.findById(id);
        if (prm == null || !prm.product.id.equals(productId)) {
            throw new NotFoundException("Raw material link not found");
        }
        prm.delete();
    }
}
