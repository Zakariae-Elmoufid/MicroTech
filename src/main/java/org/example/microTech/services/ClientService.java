package org.example.microTech.services;

import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;

public interface ClientService {

    public ClientResponseDTO createClient(ClientCreateDTO dto);
}
