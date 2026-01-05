package org.example.microTech.services;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.microTech.dto.PromoCodeRequestDTO;
import org.example.microTech.dto.PromoCodeResponseDTO;
import org.example.microTech.entities.PromoCode;
import org.example.microTech.enums.PromoCodeStatus;
import org.example.microTech.exceptions.BusinessException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.PromoCodeMapper;
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PromoCodeServiceImpl implements  PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final OrderRepository orderRepository;
    private final PromoCodeMapper promoCodeMapper;



    public PromoCodeResponseDTO createPromo(PromoCodeRequestDTO request){
        if (request.startDate().isAfter(request.endDate())) {
             new BadRequestException("startDate must be before endDate");
        }
        PromoCode promoCode = PromoCode.builder()
                .promoCode(request.promoCode())
                .discount(request.discount())
                .maxUses(request.maxUses())
                .StartDate(request.startDate())
                .EndDate(request.endDate())
                .status( PromoCodeStatus.ACTIVE)
                .created(LocalDateTime.now())
                .build();
        return promoCodeMapper.toDTO(promoCodeRepository.save(promoCode));
    }




    public PromoCode validatePromoForClient(Long clientId, String code) {

        PromoCode promo = promoCodeRepository.findByPromoCode(code);
        if (promo == null) return null;

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
