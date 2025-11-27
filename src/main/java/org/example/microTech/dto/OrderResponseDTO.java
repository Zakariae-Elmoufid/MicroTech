package org.example.microTech.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        int id,
        long clientId,
        LocalDateTime orderDate,
        BigDecimal subTotal,
        BigDecimal discount,
        BigDecimal tvaAmount,
        BigDecimal totalHT,
        BigDecimal total,
        BigDecimal remainingAmount,
        List<OrderItemsResponseDTO> orderItem
) {
}
