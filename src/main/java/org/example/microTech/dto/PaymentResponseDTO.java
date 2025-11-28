package org.example.microTech.dto;

import org.example.microTech.enums.PaymentStatus;
import org.example.microTech.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        long id,
        int paymentNumber,
        BigDecimal amount,
        PaymentType paymentType,
        String reference,
        LocalDate dueDate,
        LocalDateTime paymentDate,
        String bank,
        PaymentStatus paymentStatus
) {
}
