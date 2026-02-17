package com.autoflex.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductRawMaterialRequestDTO {

    @NotNull
    public Long rawMaterialId;

    @NotNull
    @Positive
    public Integer quantityRequired;
}
