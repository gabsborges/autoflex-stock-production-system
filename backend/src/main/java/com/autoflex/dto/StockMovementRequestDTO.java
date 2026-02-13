package com.autoflex.dto;

import com.autoflex.entity.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StockMovementRequestDTO {

    @NotNull
    public Long productId;

    @NotNull
    public MovementType type;

    @NotNull
    @Positive
    public Integer quantity;

    public String description;
}
