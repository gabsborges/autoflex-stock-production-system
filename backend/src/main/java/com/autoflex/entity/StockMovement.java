package com.autoflex.entity;

import jakarta.persistence.*;

@Entity
public class StockMovement extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    public Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public MovementType type;

    @Column(nullable = false)
    public Integer quantity;

    @Column(length = 500)
    public String description;
}
