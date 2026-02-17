package com.autoflex.dto;

import java.time.LocalDateTime;
import com.autoflex.entity.StockMovement.MovementType;

public class StockMovementResponseDTO {

    public Long id;
    public Long productId;
    public Integer quantity;
    public MovementType type;
    public LocalDateTime movementDate;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
