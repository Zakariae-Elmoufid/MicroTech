package org.example.microTech.mappers;

import ch.qos.logback.core.model.ComponentModel;
import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;
import org.example.microTech.dto.ProductRequestDTO;
import org.example.microTech.dto.ProductResponseDTO;
import org.example.microTech.entities.Client;
import org.example.microTech.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")

public interface ProductMapper {
    ProductResponseDTO toDTO(Product product);
    Product toEntity(ProductRequestDTO DTO);

}
