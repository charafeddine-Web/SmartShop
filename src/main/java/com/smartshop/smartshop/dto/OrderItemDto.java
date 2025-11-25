package com.smartshop.smartshop.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Long id;

    @NotNull(message = "Product ID must be provided")
    private Long productId;

    private Long orderId;

    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price must be provided")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Total line must be provided")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total line must be positive")
    private BigDecimal totalLine;
}
