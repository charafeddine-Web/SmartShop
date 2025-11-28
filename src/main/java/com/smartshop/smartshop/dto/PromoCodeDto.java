package com.smartshop.smartshop.dto;

import jakarta.validation.constraints.NotNull;

public class PromoCodeDto {

    private Long id;
    @NotNull(message = "Promo code must not be null")
    private String code;

    private Boolean availabilityStatus;

}
