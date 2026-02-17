package com.autoflex.service;

import com.autoflex.dto.RawMaterialRequestDTO;
import com.autoflex.dto.RawMaterialResponseDTO;
import com.autoflex.entity.RawMaterial;
import com.autoflex.mapper.RawMaterialMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    @Transactional
    public RawMaterialResponseDTO create(RawMaterialRequestDTO dto) {

        RawMaterial existing = RawMaterial.find("name", dto.name).firstResult();

        if (existing != null) {
            throw new BadRequestException("Raw material already exists");
        }

        RawMaterial rm = RawMaterialMapper.toEntity(dto);
        rm.persist();

        return RawMaterialMapper.toResponse(rm);
    }

    public List<RawMaterialResponseDTO> listAll() {
        return RawMaterial.<RawMaterial>listAll()
                .stream()
                .map(RawMaterialMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RawMaterialResponseDTO findById(Long id) {
        RawMaterial rm = RawMaterial.findById(id);

        if (rm == null) {
            throw new NotFoundException("Raw material not found");
        }

        return RawMaterialMapper.toResponse(rm);
    }

    @Transactional
    public RawMaterialResponseDTO update(Long id, RawMaterialRequestDTO dto) {

        RawMaterial rm = RawMaterial.findById(id);

        if (rm == null) {
            throw new NotFoundException("Raw material not found");
        }

        RawMaterial existing = RawMaterial.find("name", dto.name).firstResult();

        if (existing != null && !existing.id.equals(id)) {
            throw new BadRequestException("Raw material already exists");
        }

        rm.name = dto.name;
        rm.quantity = dto.quantity;

        return RawMaterialMapper.toResponse(rm);
    }

    @Transactional
    public void delete(Long id) {

        RawMaterial rm = RawMaterial.findById(id);

        if (rm == null) {
            throw new NotFoundException("Raw material not found");
        }

        rm.delete();
    }
}
