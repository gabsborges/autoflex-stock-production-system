package com.autoflex.dto;

import java.util.List;

public class ProductionPlanResponseDTO {

    public List<ProductProducibleDTO> products;
    public Double grandTotal;

    public ProductionPlanResponseDTO(
            List<ProductProducibleDTO> products,
            Double grandTotal) {
        this.products = products;
        this.grandTotal = grandTotal;
    }
}
