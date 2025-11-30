package com.smartshop.smartshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "promo_codes")
@Data
public class PromoCode {
    @Id
    @GeneratedValue( strategy = IDENTITY )
    private Long id;

    private String code;
    private Boolean availabilityStatus;

    @OneToOne
    private Order order;
}
