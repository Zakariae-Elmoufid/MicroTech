package org.example.microTech.dto;

import org.example.microTech.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        long id,
        Long clientId,
        OrderStatus orderStatus,
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
