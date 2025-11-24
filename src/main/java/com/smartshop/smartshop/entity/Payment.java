package com.smartshop.smartshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Payment {
    @Id
    private Long id;

    private Long orderId;
    private Long paymentNumber;
    private Double amountPaid;
    private String paymentMethod;
    private String paymentDate;

}
