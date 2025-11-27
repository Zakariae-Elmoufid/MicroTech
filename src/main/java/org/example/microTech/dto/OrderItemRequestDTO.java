package org.example.microTech.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequestDTO(
        long productId,
        int quantity,
        BigDecimal unitPrice

) {
}
