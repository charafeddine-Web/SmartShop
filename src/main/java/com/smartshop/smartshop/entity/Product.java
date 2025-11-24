package com.smartshop.smartshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;

}
