package com.smartshop.smartshop.entity;

import com.smartshop.smartshop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "clients")
public class Client  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerTier fidelityLevel;

    @Column(nullable = false)
    private Integer totalOrders = 0;

    @Column(nullable = false)
    private BigDecimal totalSpent = BigDecimal.ZERO;

}
