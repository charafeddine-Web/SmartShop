package com.smartshop.smartshop.dto;

import com.smartshop.smartshop.entity.enums.OrderStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {

    private Long id;

    @NotNull(message = "Client ID must be provided")
    private Long clientId;

    @NotNull(message = "Order date must be provided")
    private LocalDateTime orderDate;

    @NotNull(message = "Subtotal must be provided")
    @DecimalMin(value = "0.0", inclusive = true, message = "Subtotal must be positive")
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount must be positive")
    private BigDecimal discount;

    @DecimalMin(value = "0.0", inclusive = true, message = "TVA must be positive")
    private BigDecimal tva;

    @DecimalMin(value = "0.0", inclusive = true, message = "Promo code value must be positive")
    private BigDecimal promoCode;

    @NotNull(message = "Total must be provided")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total must be positive")
    private BigDecimal total;

    @DecimalMin(value = "0.0", inclusive = true, message = "Remaining amount must be positive")
    private BigDecimal remainingAmount;

    @NotNull(message = "Order status must be provided")
    private OrderStatus status;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemDto> items;

    private List<PaymentDto> payments;
}
