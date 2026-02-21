package com.autoflex.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
public class Product extends PanacheEntity {

    @NotNull
    @Column(nullable = false)
    public String name;

    @NotNull
    @Column(nullable = false, unique = true)
    public String sku;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    public Integer quantity;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    public Double price;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(
    mappedBy = "product",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY
)
    public List<ProductRawMaterial> rawMaterials;
}
