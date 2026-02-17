package com.autoflex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RawMaterialRequestDTO {

    @NotBlank
    public String name;

    @NotNull
    @Positive
    public Integer quantity;
}
