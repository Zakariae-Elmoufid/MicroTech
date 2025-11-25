package org.example.microTech.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
        long id,
        String name,
        BigDecimal unitPrice,
        int stock,
        boolean active

        ) {


}
