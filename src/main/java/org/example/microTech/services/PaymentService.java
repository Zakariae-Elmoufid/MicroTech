package org.example.microTech.services;

import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    public PaymentResponseDTO createPayment(long orderId,PaymentRequestDTO request);
    public PaymentResponseDTO getPaymentById(Long id);
    public List<PaymentResponseDTO> getPaymentsByOrderId(Long orderId);
    public Page<PaymentResponseDTO> getAllPayments(int page, int size);
}
