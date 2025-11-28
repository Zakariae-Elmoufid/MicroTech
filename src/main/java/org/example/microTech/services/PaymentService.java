package org.example.microTech.services;

import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;

import java.math.BigDecimal;

public interface PaymentService {
    public PaymentResponseDTO createPayment(long orderId,PaymentRequestDTO request);
}
