package org.example.microTech.services;

import lombok.AllArgsConstructor;
import org.example.microTech.entities.Client;
import org.example.microTech.entities.PromoCode;
import org.example.microTech.enums.CustomerTier;
import org.example.microTech.enums.PromoCodeStatus;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.repositories.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class DiscountServiceImpl implements   DiscountService {

    private static final BigDecimal SILVER_RATE = new BigDecimal("0.05");
    private static final BigDecimal GOLD_RATE = new BigDecimal("0.10");
    private static final BigDecimal PLATINUM_RATE = new BigDecimal("0.15");

    private final PromoCodeRepository promoCodeRepository ;

    public BigDecimal calculateTotalDiscountRate(Client client, BigDecimal subTotal, PromoCode promo){
        BigDecimal loyaltyDiscountRate = calculateLoyaltyDiscountRate(client.getLoyaltyLevel(), subTotal);
        BigDecimal promoDiscountRate = (promo != null) ? promo.getDiscount() : BigDecimal.ZERO;

        return loyaltyDiscountRate.add(promoDiscountRate);
    }

    private BigDecimal calculateLoyaltyDiscountRate(CustomerTier level, BigDecimal subTotal) {
       switch (level) {
           case PLATINUM:
               if (subTotal.compareTo(new BigDecimal("1200.00")) >= 0) {
                   return PLATINUM_RATE;
               }
               break;
           case GOLD:
               if (subTotal.compareTo(new BigDecimal("800.00")) >= 0) {
                   return  GOLD_RATE;
               }
               break;
           case SILVER:
               if (subTotal.compareTo(new BigDecimal("500.00")) >= 0) {
                   return SILVER_RATE;
               }
               break;
           default:
               return BigDecimal.ZERO;
       }
       return BigDecimal.ZERO;
    }

    public PromoCode validateAndGetPromo(String promoCodeString) {
        if (promoCodeString == null || promoCodeString.trim().isEmpty()) {
            return null;
        }

        return promoCodeRepository.findByPromoCodeAndStatus(
                promoCodeString,
                PromoCodeStatus.ACTIVE
        ).orElseThrow(
                () -> new ResourceNotFoundException("Promo code '" + promoCodeString + "' is invalid, expired, or not found.")
        );
    }
}
