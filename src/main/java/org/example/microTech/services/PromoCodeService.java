package org.example.microTech.services;

import org.example.microTech.entities.PromoCode;

public interface PromoCodeService {
    public PromoCode validatePromoForClient(Long clientId, String code);

}
