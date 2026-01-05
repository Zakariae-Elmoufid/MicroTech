package org.example.microTech.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromoCodeRequestDTO(
        @Pattern(regexp = "PROMO-[A-Z0-9]{4}",
                message = "Promo code must follow the format PROMO-XXXX (4 uppercase letters or digits)")
        String promoCode,

        @NotNull(message = "Discount cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be positive")
        BigDecimal discount,
        @Positive(message = "maxUses must be greater than 0")
        int maxUses,

        @NotNull(message = "startDate cannot be null")
        LocalDateTime startDate,
        @NotNull(message = "endDate cannot be null")
        LocalDateTime endDate

) {
}
