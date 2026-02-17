package com.autoflex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "raw_material")
public class RawMaterial extends BaseEntity {
    @NotNull
    @Column(nullable = false, unique = true)
    public String name;

    @NotNull
    @Positive
    @Column(nullable = false)
    public Integer quantity; 
}