package com.smartshop.smartshop.dto;

import com.smartshop.smartshop.entity.enums.CustomerTier;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ClientDto {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Fidelity level must be specified")
    private CustomerTier fidelityLevel;

    @NotNull(message = "Total orders cannot be null")
    @Min(value = 0, message = "Total orders must be >= 0")
    private Integer totalOrders;

    @NotNull(message = "Total spent cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total spent must be >= 0")
    private BigDecimal totalSpent;
}
