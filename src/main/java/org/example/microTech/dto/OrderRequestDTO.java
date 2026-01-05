package org.example.microTech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;
public record OrderRequestDTO
        (
                long clientId,
        @Pattern(regexp = "PROMO-[A-Z0-9]{4}",
        message = "Promo code must follow the format PROMO-XXXX (4 uppercase letters or digits)")
        String promoCode,

        @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
        BigDecimal  tva,

        @NotNull(message = "items must be provided")
        List<OrderItemRequestDTO> items

        ){}
