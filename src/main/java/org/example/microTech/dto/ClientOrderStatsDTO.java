package org.example.microTech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClientOrderStatsDTO(
        int totalOrdersConfirmed,
        int totalOrdersPending,
        BigDecimal cumulativeAmount,
        LocalDateTime firstOrderDate,
        LocalDateTime  lastOrderDate,
        List<OrderHistoryDTO> orderHistory



) {
}
