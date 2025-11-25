package com.smartshop.smartshop.dto;

import com.smartshop.smartshop.entity.enums.UserRole;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotNull(message = "User role must be specified")
    private UserRole role;
}
