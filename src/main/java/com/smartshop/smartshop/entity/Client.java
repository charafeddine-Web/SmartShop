package com.smartshop.smartshop.entity;

import com.smartshop.smartshop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "clients")
public class Client extends User {



    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier fidelityLevel;

    private Integer totalOrders;

    private BigDecimal totalSpent;

}
