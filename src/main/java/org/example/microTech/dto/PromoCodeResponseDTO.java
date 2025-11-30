package org.example.microTech.dto;

import org.example.microTech.entities.PromoCode;
import org.example.microTech.enums.PromoCodeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromoCodeResponseDTO(
     long id,
     LocalDateTime startDate,
     LocalDateTime endDate,
     String promoCode,
     LocalDateTime createdAt,
     BigDecimal discount,
     int maxUses,
     int currentUses,
     PromoCodeStatus promoCodeStatus
) {
}
