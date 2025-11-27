package org.example.microTech.mappers;


import org.example.microTech.dto.OrderResponseDTO;
import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.example.microTech.entities.Order;
import org.example.microTech.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    public PaymentResponseDTO toDto(Payment payment);
    public Payment toEntity(PaymentRequestDTO dto);
}
