package com.autoflex.mapper;

import com.autoflex.dto.StockMovementResponseDTO;
import com.autoflex.entity.StockMovement;

public class StockMovementMapper {

    public static StockMovementResponseDTO toResponse(StockMovement sm) {
        StockMovementResponseDTO dto = new StockMovementResponseDTO();
        dto.id = sm.id;
        dto.productId = sm.product.id;
        dto.quantity = sm.quantity;
        dto.type = sm.type;
        dto.movementDate = sm.movementDate;
        dto.createdAt = sm.createdAt;
        dto.updatedAt = sm.updatedAt;
        return dto;
    }
}
