package com.smartshop.smartshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartshop.smartshop.dto.spec.Creation;
import com.smartshop.smartshop.entity.enums.CustomerTier;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ClientDto {

    @Null(message = "Id must be null when creating a new client")
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(groups = Creation.class,message = "password cannot be blank")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    private CustomerTier fidelityLevel;

    @NotNull(message = "Total orders cannot be null")
    @Min(value = 0, message = "Total orders must be >= 0")
    private Integer totalOrders;

    @NotNull(message = "Total spent cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total spent must be >= 0")
    private BigDecimal totalSpent;
}
