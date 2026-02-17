package com.autoflex.mapper;

import com.autoflex.dto.RawMaterialRequestDTO;
import com.autoflex.dto.RawMaterialResponseDTO;
import com.autoflex.entity.RawMaterial;

public class RawMaterialMapper {

    public static RawMaterial toEntity(RawMaterialRequestDTO dto) {
        RawMaterial rm = new RawMaterial();
        rm.name = dto.name;
        rm.quantity = dto.quantity;
        return rm;
    }

    public static RawMaterialResponseDTO toResponse(RawMaterial rm) {
        RawMaterialResponseDTO dto = new RawMaterialResponseDTO();
        dto.id = rm.id;
        dto.name = rm.name;
        dto.quantity = rm.quantity;
        dto.createdAt = rm.createdAt;
        dto.updatedAt = rm.updatedAt;
        return dto;
    }
}
