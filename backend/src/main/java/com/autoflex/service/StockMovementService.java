package com.autoflex.service;

import java.util.List;
import java.util.stream.Collectors;

import com.autoflex.dto.StockMovementRequestDTO;
import com.autoflex.dto.StockMovementResponseDTO;
import com.autoflex.entity.*;
import com.autoflex.mapper.StockMovementMapper;
import com.autoflex.repository.ProductRepository;
import com.autoflex.repository.StockMovementRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class StockMovementService {

    @Inject
    ProductRepository productRepository;

    @Inject
    StockMovementRepository movementRepository;

    @Transactional
    public void registerMovement(StockMovementRequestDTO dto) {

        Product product = productRepository.findById(dto.productId);

        if (product == null) {
            throw new BadRequestException("Product not found");
        }

        if (dto.type == MovementType.OUT && product.quantity < dto.quantity) {
            throw new BadRequestException("Insufficient stock");
        }

        if (dto.type == MovementType.IN) {
            product.quantity += dto.quantity;
        } else {
            product.quantity -= dto.quantity;
        }

        StockMovement movement = new StockMovement();
        movement.product = product;
        movement.type = dto.type;
        movement.quantity = dto.quantity;
        movement.description = dto.description;

        movementRepository.persist(movement);
    }

    public List<StockMovementResponseDTO> listByProduct(Long productId) {

    Product product = productRepository.findById(productId);

    if (product == null) {
        throw new BadRequestException("Product not found");
    }

    return movementRepository.findByProductId(productId)
            .stream()
            .map(StockMovementMapper::toResponse)
            .collect(Collectors.toList());
}
}
