package com.smartshop.smartshop.dto;

import com.smartshop.smartshop.entity.enums.PaymentMethod;
import com.smartshop.smartshop.entity.enums.PaymentStatus;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDto {

    private Long id;

    @NotNull(message = "Order ID must be provided")
    private Long orderId;

    @NotNull(message = "Payment number must be provided")
    @Positive(message = "Payment number must be positive")
    private Long paymentNumber;

    @NotNull(message = "Amount paid must be provided")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amountPaid;

    @NotNull(message = "Payment method must be provided")
    private PaymentMethod paymentMethod;

    private LocalDateTime paymentDate;

    private LocalDateTime dateEncaissement;

    @NotNull(message = "Payment status must be provided")
    private PaymentStatus status;
}
