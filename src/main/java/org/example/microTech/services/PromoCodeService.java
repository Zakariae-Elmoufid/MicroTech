package org.example.microTech.services;

import org.example.microTech.dto.PromoCodeRequestDTO;
import org.example.microTech.dto.PromoCodeResponseDTO;
import org.example.microTech.entities.PromoCode;

public interface PromoCodeService {
    public PromoCode validatePromoForClient(Long clientId, String code);
    public PromoCodeResponseDTO createPromo(PromoCodeRequestDTO request);

}
