package org.example.microTech.dto;

import org.example.microTech.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderHistoryDTO(
        long id,
         LocalDateTime orderDate,
         BigDecimal totalht,
         BigDecimal discount,
         BigDecimal subTotal,
         BigDecimal tvaAmount,
         BigDecimal remainingAmount,
         BigDecimal total,
         OrderStatus orderStatus



) {
}
