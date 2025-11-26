package org.example.microTech.dto;

import java.math.BigDecimal;

public record OrderItemRequestDTO(
        long productId,
        int qauntity,
        BigDecimal unitPrice
) {
}
