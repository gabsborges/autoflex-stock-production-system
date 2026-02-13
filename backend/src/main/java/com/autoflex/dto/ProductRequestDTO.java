package com.autoflex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductRequestDTO {

    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "SKU is required")
    public String sku;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    public Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    public Double price;
}
