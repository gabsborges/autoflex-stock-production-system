package com.autoflex.repository;

import java.util.List;

import com.autoflex.entity.StockMovement;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StockMovementRepository implements PanacheRepository<StockMovement> {  


    public List<StockMovement> findByProductId(Long productId) {
    return find("product.id", productId).list();
}
}
