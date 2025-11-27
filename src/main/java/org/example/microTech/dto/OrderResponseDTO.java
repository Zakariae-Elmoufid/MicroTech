package org.example.microTech.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponseDTO(
        int id,
        LocalDateTime orderDate,
        BigDecimal subTotal,
        BigDecimal discount,
        BigDecimal tvaAmount,
        BigDecimal totalHT,
        BigDecimal total,
        BigDecimal remainingAmount


) {
}
