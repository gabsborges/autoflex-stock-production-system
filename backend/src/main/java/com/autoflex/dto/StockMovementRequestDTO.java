package com.autoflex.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import com.autoflex.entity.StockMovement.MovementType;

public class StockMovementRequestDTO {

    @NotNull
    public Long productId;

    @NotNull
    @PositiveOrZero
    public Integer quantity;

    @NotNull
    public MovementType type;

    public LocalDateTime movementDate;
}
