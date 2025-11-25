package org.example.microTech.mappers;


import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;
import org.example.microTech.entities.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientResponseDTO toDto(Client client);
    Client toEntity(ClientCreateDTO DTO);
}
