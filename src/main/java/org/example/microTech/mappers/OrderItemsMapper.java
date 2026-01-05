package org.example.microTech.mappers;


import org.example.microTech.dto.OrderItemsResponseDTO;
import org.example.microTech.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper {
    @Mapping(source = "product.name", target = "productName")
    OrderItemsResponseDTO toDTO(OrderItem  line);
    List<OrderItemsResponseDTO> toDTO(List<OrderItem>  lines);
}
