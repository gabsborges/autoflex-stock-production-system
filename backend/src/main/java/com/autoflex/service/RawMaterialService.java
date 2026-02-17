package com.autoflex.service;

import com.autoflex.dto.RawMaterialRequestDTO;
import com.autoflex.dto.RawMaterialResponseDTO;
import com.autoflex.entity.RawMaterial;
import com.autoflex.mapper.RawMaterialMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    @Transactional
    public RawMaterialResponseDTO create(RawMaterialRequestDTO dto) {
        RawMaterial rm = RawMaterialMapper.toEntity(dto);
        rm.persist();
        return RawMaterialMapper.toResponse(rm);
    }

    public List<RawMaterialResponseDTO> listAll() {
        return RawMaterial.listAll()
                .stream()
                .map(r -> (RawMaterial) r)
                .map(RawMaterialMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RawMaterial findEntityById(Long id) {
        RawMaterial rm = RawMaterial.findById(id);
        if (rm == null) throw new RuntimeException("RawMaterial not found");
        return rm;
    }
}
