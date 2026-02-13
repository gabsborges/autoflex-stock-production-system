package com.autoflex.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
public class Product extends BaseEntity {

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String sku;

    @Column(nullable = false)
    public Integer quantity;

    @Column(nullable = false)
    public Double price;
}
