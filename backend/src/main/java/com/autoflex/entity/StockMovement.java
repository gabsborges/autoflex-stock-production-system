package com.autoflex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movement")
public class StockMovement extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @NotNull
    @Column(nullable = false)
    public LocalDateTime movementDate = LocalDateTime.now();

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    public Integer quantity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "raw_material_id")
    public RawMaterial rawMaterial;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public MovementType type;

    public enum MovementType {
        IN,
        OUT
    }
}
