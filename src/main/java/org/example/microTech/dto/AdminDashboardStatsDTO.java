package org.example.microTech.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminDashboardStatsDTO(
        long confirmedOrders,
        long pendingOrders,
        long canceledOrders,
        long rejectedOrders,
        BigDecimal totalConfirmedAmount,
        LocalDateTime firstOrderDate,
        LocalDateTime lastOrderDate
) {}
