package com.smartshop.smartshop.entity;

import com.smartshop.smartshop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDateTime orderDate;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal tva;

    private BigDecimal promoCode;

    private BigDecimal total;

    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
