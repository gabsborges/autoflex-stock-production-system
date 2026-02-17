package com.autoflex.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "product_raw_material",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "raw_material_id"})})
public class ProductRawMaterial extends PanacheEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    public RawMaterial rawMaterial;

    @NotNull
    @Positive
    @Column(nullable = false)
    public Integer quantityRequired;
}
