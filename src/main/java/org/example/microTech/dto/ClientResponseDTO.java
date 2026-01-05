package org.example.microTech.dto;

import org.example.microTech.enums.CustomerTier;

public record ClientResponseDTO (
    Long id,
    String name,
    String email,
    CustomerTier loyaltyLevel
){};
