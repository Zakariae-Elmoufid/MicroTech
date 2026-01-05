package org.example.microTech.repositories;

import org.example.microTech.entities.PromoCode;
import org.example.microTech.enums.PromoCodeStatus;
import org.example.microTech.enums.PromoCodeStatus.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static org.example.microTech.enums.PromoCodeStatus.ACTIVE;

public interface PromoCodeRepository extends CrudRepository<PromoCode,Long> {
    public PromoCode   findByPromoCode(String promoCode);

}
