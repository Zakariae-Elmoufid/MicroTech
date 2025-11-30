package org.example.microTech.mappers;


import org.example.microTech.dto.PromoCodeResponseDTO;
import org.example.microTech.entities.PromoCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  PromoCodeMapper {
    PromoCodeResponseDTO toDTO(PromoCode promoCode);
}
