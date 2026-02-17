package com.autoflex.dto;

public class ProductProducibleDTO {
    public Long id;
    public String name;
    public String sku;
    public Integer quantityProducible;
    public Double price;
    public Double totalValue;

    public ProductProducibleDTO() {}

    public ProductProducibleDTO(Long id, String name, String sku, Integer quantityProducible, Double price) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.quantityProducible = quantityProducible;
        this.price = price;
        this.totalValue = quantityProducible * price;
    }

    // Getter necessário para Comparator
    public Double getTotalValue() {
        return totalValue;
    }
}
