package com.autoflex.service;

import com.autoflex.dto.StockMovementRequestDTO;
import com.autoflex.dto.StockMovementResponseDTO;
import com.autoflex.entity.Product;
import com.autoflex.entity.StockMovement;
import com.autoflex.mapper.StockMovementMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StockMovementService {

    @Transactional
    public StockMovementResponseDTO create(StockMovementRequestDTO dto) {
        Product product = Product.findById(dto.productId);
        if (product == null) throw new NotFoundException("Product not found");

        StockMovement sm = new StockMovement();
        sm.product = product;
        sm.quantity = dto.quantity;
        sm.type = dto.type;
        sm.movementDate = dto.movementDate != null ? dto.movementDate : java.time.LocalDateTime.now();
        sm.persist();

        return StockMovementMapper.toResponse(sm);
    }

    public List<StockMovementResponseDTO> listByProduct(Long productId) {
        Product product = Product.findById(productId);
        if (product == null) throw new NotFoundException("Product not found");

        return StockMovement.find("product", product)
                .list()
                .stream()
                .map(sm -> StockMovementMapper.toResponse((StockMovement) sm))
                .collect(Collectors.toList());
    }

    public int getCurrentStock(Long productId) {
        Product product = Product.findById(productId);
        if (product == null) throw new NotFoundException("Product not found");

        List<StockMovement> movements = StockMovement.find("product", product).list();

        int stock = 0;
        for (StockMovement sm : movements) {
            if (sm.type == StockMovement.MovementType.IN) stock += sm.quantity;
            else stock -= sm.quantity;
        }
        return stock;
    }
}
