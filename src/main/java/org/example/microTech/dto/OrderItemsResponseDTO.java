package org.example.microTech.dto;

import java.math.BigDecimal;

public record OrderItemsResponseDTO (
    long  id,
    int quantity,
    BigDecimal unitPrice,
    String productName,
    BigDecimal totalLine
){

}
