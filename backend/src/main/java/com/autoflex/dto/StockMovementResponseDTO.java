package com.autoflex.dto;

import com.autoflex.entity.MovementType;
import java.time.LocalDateTime;

public class StockMovementResponseDTO {

    public Long id;
    public MovementType type;
    public Integer quantity;
    public String description;
    public LocalDateTime createdAt;
}
