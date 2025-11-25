package org.example.microTech.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Product name is required")
        @Size(min = 5, max = 50, message = "Product name must be between 5 and 50 characters")
        String name,

        @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
        BigDecimal unitPrice,

        @Min(value = 0, message = "The value must be greater than or equal to 0")
        Integer stock


) {
}
