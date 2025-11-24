package com.smartshop.smartshop.entity;

import com.smartshop.smartshop.entity.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.text.DecimalFormat;

@Data
@Entity
public class Order {
    @Id
    private Long id;

    private Long clientId;
    private String orderDate;
    private DecimalFormat subtotal;
    private Double totalAmount;
    private DecimalFormat discount;
    private DecimalFormat TVA;
    private DecimalFormat promoCode;
    private DecimalFormat Total;
    private OrderStatus status;
    private DecimalFormat remainingAmount;
}
