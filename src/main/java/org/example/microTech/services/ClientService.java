package org.example.microTech.services;

import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;
import org.example.microTech.dto.ClientUpdateDTO;

import java.util.List;

public interface ClientService {

    public ClientResponseDTO createClient(ClientCreateDTO dto);
    public List<ClientResponseDTO> getAllClients();
    public ClientResponseDTO getClientById(long id);
    public  ClientResponseDTO updateClient(long id , ClientUpdateDTO dto);
    public ClientResponseDTO deleteClient(long id);
}
