package com.autoflex.service;

import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.entity.Product;
import com.autoflex.mapper.ProductMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {

        if (dto == null) {
            throw new BadRequestException("Product data is required");
        }

        String normalizedSku = dto.sku.trim();

        boolean skuExists = Product.find("sku", normalizedSku)
                .firstResultOptional()
                .isPresent();

        if (skuExists) {
            throw new BadRequestException("SKU already exists");
        }

        Product product = ProductMapper.toEntity(dto);
        product.sku = normalizedSku;

        product.persist();

        return ProductMapper.toResponse(product);
    }

    public List<ProductResponseDTO> listAll() {
        return Product.listAll()
                .stream()
                .map(product -> ProductMapper.toResponse((Product) product))
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> listPaged(int page, int size, String sortBy, String direction) {
        String sortOrder = direction.equalsIgnoreCase("desc") ? "desc" : "asc";
        return Product.find("ORDER BY " + sortBy + " " + sortOrder)
                .page(page, size)
                .list()
                .stream()
                .map(product -> ProductMapper.toResponse((Product) product))
                .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product product = Product.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return ProductMapper.toResponse(product);
    }

    public ProductResponseDTO findBySku(String sku) {
        Product product = Product.find("sku", sku).firstResult();
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return ProductMapper.toResponse(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {

        Product product = Product.findById(id);

        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        String normalizedSku = dto.sku.trim();

        Product existingProduct = Product.find("sku", normalizedSku).firstResult();

        // Só bloqueia se o SKU já existir EM OUTRO produto
        if (existingProduct != null && !existingProduct.id.equals(id)) {
            throw new BadRequestException("SKU already exists");
        }

        product.name = dto.name;
        product.sku = normalizedSku;
        product.quantity = dto.quantity;
        product.price = dto.price;

        return ProductMapper.toResponse(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = Product.findById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        product.delete();
    }
}
