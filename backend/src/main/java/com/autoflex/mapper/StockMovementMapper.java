package com.autoflex.mapper;

import com.autoflex.dto.StockMovementResponseDTO;
import com.autoflex.entity.StockMovement;

public class StockMovementMapper {

    public static StockMovementResponseDTO toResponse(StockMovement movement) {
        StockMovementResponseDTO dto = new StockMovementResponseDTO();
        dto.id = movement.id;
        dto.type = movement.type;
        dto.quantity = movement.quantity;
        dto.description = movement.description;
        dto.createdAt = movement.createdAt;
        return dto;
    }
}
