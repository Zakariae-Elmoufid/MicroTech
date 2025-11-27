package org.example.microTech.mappers;

import org.example.microTech.dto.OrderRequestDTO;
import org.example.microTech.dto.OrderResponseDTO;
import org.example.microTech.entities.Order;
import org.example.microTech.entities.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring" , uses = OrderItemsMapper.class)
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "orderItem")
    OrderResponseDTO toDto(Order order);
    @Mapping(target = "promoCode", source = "promoCode", qualifiedByName = "mapPromoCode")
    Order toEntity(OrderRequestDTO dto);
    @Named("mapPromoCode")
    default PromoCode mapPromoCode(String code) {
        if (code == null) return null;
        PromoCode promo = new PromoCode();
        promo.setPromoCode(code);
        return promo;
    }
}
