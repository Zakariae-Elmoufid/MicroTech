package org.example.microTech.services;

import org.example.microTech.entities.Client;
import org.example.microTech.entities.PromoCode;

import java.math.BigDecimal;

public interface DiscountService {
    public PromoCode validateAndGetPromo(String promoCodeString);
    public BigDecimal calculateTotalDiscountRate(Client client, BigDecimal subTotal, PromoCode promo);
}
