package com.autoflex.service;

import com.autoflex.dto.ProductResponseDTO;
import com.autoflex.entity.Product;
import com.autoflex.entity.ProductRawMaterial;
import com.autoflex.entity.RawMaterial;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

@ApplicationScoped
public class ProductionService {

    public Map<ProductResponseDTO, Integer> suggestProduction() {
        Map<ProductResponseDTO, Integer> suggestion = new LinkedHashMap<>();

        // Pega todos os produtos ativos
        List<Product> products = Product.listAll();

        // Ordena produtos por preço decrescente para priorizar os mais valiosos
        products.sort((p1, p2) -> Double.compare(p2.price, p1.price));


        // Cria um mapa temporário do estoque de matérias-primas
        Map<Long, Integer> tempStock = new HashMap<>();
        List<RawMaterial> rawMaterials = RawMaterial.listAll();
        for (RawMaterial rm : rawMaterials) {
            tempStock.put(rm.id, rm.quantity);
        }

        for (Product product : products) {
            List<ProductRawMaterial> materials = ProductRawMaterial.find("product", product).list();

            if (materials.isEmpty()) continue;

            // Calcula a quantidade máxima que pode ser produzida
            int maxProduction = Integer.MAX_VALUE;
            for (ProductRawMaterial prm : materials) {
                int available = tempStock.getOrDefault(prm.rawMaterial.id, 0);
                int possible = available / prm.quantityRequired;
                if (possible < maxProduction) maxProduction = possible;
            }

            if (maxProduction <= 0) continue;

            // Atualiza estoque temporário para simular consumo
            for (ProductRawMaterial prm : materials) {
                int remaining = tempStock.get(prm.rawMaterial.id) - (prm.quantityRequired * maxProduction);
                tempStock.put(prm.rawMaterial.id, remaining);
            }

            // Cria DTO para o resultado
            ProductResponseDTO dto = new ProductResponseDTO();
            dto.id = product.id;
            dto.name = product.name;
            dto.sku = product.sku;
            dto.quantity = maxProduction; // quantidade sugerida para produção
            dto.price = product.price;
            dto.createdAt = product.createdAt;
            dto.updatedAt = product.updatedAt;

            suggestion.put(dto, maxProduction);
        }

        return suggestion;
    }

    public double calculateTotalValue(Map<ProductResponseDTO, Integer> suggestion) {
        return suggestion.entrySet()
                .stream()
                .mapToDouble(e -> e.getKey().price * e.getValue())
                .sum();
    }
}
