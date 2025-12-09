package org.example.microTech.services;


import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;
import org.example.microTech.dto.ClientUpdateDTO;
import org.example.microTech.entities.Client;
import org.example.microTech.entities.User;
import org.example.microTech.enums.CustomerTier;
import org.example.microTech.enums.UserRole;
import org.example.microTech.mappers.ClientMapper;
import org.example.microTech.repositories.ClientRepository;
import org.example.microTech.repositories.UserRepository;
import org.example.microTech.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class  ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ClientMapper clientMapper;


    @Override
    public ClientResponseDTO createClient(ClientCreateDTO dto) {
        User user = User.builder()
                .userName(dto.username())
                .password(PasswordUtil.hash(dto.password()))
                .role(UserRole.CLIENT)
                .build();
        userRepository.save(user);

        Client client = Client.builder()
                .user(user)
                .name(dto.name())
                .email(dto.email())
                .loyaltyLevel(CustomerTier.BASIC)
                .build();

        return clientMapper.toDto(clientRepository.save(client));
    }

    public List<ClientResponseDTO> getAllClients(){
          return  clientRepository.findAll()
                  .stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    public ClientResponseDTO getClientById(long id){
       Client client = clientRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Client not found"));
       return clientMapper.toDto(client);
    }

    public  ClientResponseDTO updateClient(long id , ClientUpdateDTO dto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        if (dto.name() != null) existingClient.setName(dto.name());
        if (dto.email() != null && !dto.email().equals(existingClient.getEmail())) {
            boolean emailExists = clientRepository.existsByEmail(dto.email());
            if (emailExists) {
                throw new RuntimeException("Email is already in use");
            }
            existingClient.setEmail(dto.email());
        }
        clientRepository.save(existingClient);
        return clientMapper.toDto(existingClient);
    }



    public ClientResponseDTO deleteClient(long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.delete(client);
        return clientMapper.toDto(client);
    }
}
