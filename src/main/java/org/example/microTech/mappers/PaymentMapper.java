package org.example.microTech.mappers;


import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.example.microTech.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    public PaymentResponseDTO toDTO(Payment payment);
    public Payment toEntity(PaymentRequestDTO dto);

}
