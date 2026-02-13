package com.autoflex.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
public class Product extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String sku;

    @Column(nullable = false)
    public Integer quantity;

    @Column(nullable = false)
    public Double price;
}
