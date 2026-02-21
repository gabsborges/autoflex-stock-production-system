package com.autoflex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.autoflex.dto.ProductProducibleDTO;
import com.autoflex.dto.ProductRequestDTO;
import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.dto.ProductionPlanResponseDTO;
import com.autoflex.entity.Product;
import com.autoflex.entity.ProductRawMaterial;
import com.autoflex.mapper.ProductMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;



@ApplicationScoped
public class ProductService {

    @Inject
    ProductRawMaterialService productRawMaterialService;

    @Inject
    RawMaterialService rawMaterialService;

    @Transactional
public ProductResponseDTO create(ProductRequestDTO dto) {

    if (dto == null) {
        throw new BadRequestException("Product data is required");
    }

    String normalizedSku = dto.sku.trim();
    String normalizedName = dto.name.trim();

    boolean skuExists = Product.find("sku", normalizedSku)
            .firstResultOptional()
            .isPresent();
    if (skuExists) {
        throw new BadRequestException("SKU already exists");
    }

    boolean nameExists = Product.find("name", normalizedName)
            .firstResultOptional()
            .isPresent();
    if (nameExists) {
        throw new BadRequestException("Product name already exists");
    }

    Product product = ProductMapper.toEntity(dto);
    product.sku = normalizedSku;
    product.name = normalizedName;

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

    public ProductionPlanResponseDTO listProducibleProducts() {

        List<Product> products = findAllWithRawMaterials();

        // 1️⃣ Ordenar por maior preço
        products.sort((a, b) -> b.price.compareTo(a.price));

        // 2️⃣ Criar estoque virtual
        Map<Long, Integer> virtualStock = new HashMap<>();

        for (Product product : products) {
            if (product.rawMaterials == null)
                continue;

            for (ProductRawMaterial prm : product.rawMaterials) {
                if (prm.rawMaterial != null) {
                    virtualStock.putIfAbsent(
                            prm.rawMaterial.id,
                            prm.rawMaterial.quantity);
                }
            }
        }

        List<ProductProducibleDTO> result = new ArrayList<>();
        double grandTotal = 0.0;

        // 3️⃣ Processar produção sequencial
        for (Product product : products) {

            if (product.rawMaterials == null || product.rawMaterials.isEmpty())
                continue;

            int maxProducible = Integer.MAX_VALUE;

            // calcular gargalo baseado no estoque virtual
            for (ProductRawMaterial prm : product.rawMaterials) {

                if (prm.rawMaterial == null) {
                    maxProducible = 0;
                    break;
                }

                Long rawMaterialId = prm.rawMaterial.id;
                int available = virtualStock.getOrDefault(rawMaterialId, 0);

                int producibleByMaterial = available / prm.quantityRequired;

                maxProducible = Math.min(maxProducible, producibleByMaterial);
            }

            if (maxProducible <= 0)
                continue;

            // 4️⃣ Consumir estoque virtual
            for (ProductRawMaterial prm : product.rawMaterials) {

                Long rawMaterialId = prm.rawMaterial.id;
                int available = virtualStock.get(rawMaterialId);

                int consumed = prm.quantityRequired * maxProducible;

                virtualStock.put(rawMaterialId, available - consumed);
            }

            ProductProducibleDTO dto = new ProductProducibleDTO(
                    product.id,
                    product.name,
                    product.sku,
                    maxProducible,
                    product.price);

            result.add(dto);

            grandTotal += dto.totalValue;
        }

        return new ProductionPlanResponseDTO(result, grandTotal);
    }

    public List<Product> findAllWithRawMaterials() {
        return Product.find(
                "select distinct p from Product p " +
                        "left join fetch p.rawMaterials prm " +
                        "left join fetch prm.rawMaterial")
                .list();
    }
}
