package org.example.microTech.services;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.microTech.entities.PromoCode;
import org.example.microTech.enums.PromoCodeStatus;
import org.example.microTech.exceptions.BusinessException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PromoCodeServiceImpl implements  PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final OrderRepository orderRepository;


    public PromoCode validatePromoForClient(Long clientId, String code) {

        PromoCode promo = promoCodeRepository.findByPromoCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Promo code not found"));

        if (promo.getStatus() != PromoCodeStatus.ACTIVE) {
            throw new BusinessException("Promo code is not active");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promo.getStartDate()) || now.isAfter(promo.getEndDate())) {
            throw new BusinessException("Promo code is expired or not yet valid");
        }

        promo.setCurrentUses(promo.getCurrentUses() + 1);
        if (promo.getCurrentUses() >= promo.getMaxUses()) {
            promo.setStatus(PromoCodeStatus.EXPIRED);
            promoCodeRepository.save(promo);
            throw new BusinessException("Promo code has reached maximum usage");
        }

        int alreadyUsed = orderRepository.countClientPromoUsage(clientId, promo.getId());
        if (alreadyUsed > 0) {
            throw new BusinessException("This client has already used this promo code");
        }

        promoCodeRepository.save(promo);

        return promo;
    }



}
