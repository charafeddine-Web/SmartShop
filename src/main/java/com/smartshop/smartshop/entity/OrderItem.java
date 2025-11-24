package com.smartshop.smartshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OrderItem {

    @Id
    private Long id;
    private Long productId;
    private Long orderId;
    private Integer quantity;
    private Double price;
    private String totalLine;

}
