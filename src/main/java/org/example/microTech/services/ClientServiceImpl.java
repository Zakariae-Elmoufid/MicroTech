package org.example.microTech.services;

import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;
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

@Service
public class ClientServiceImpl implements ClientService {

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
        userRepository.save(user);

        return clientMapper.toDto(clientRepository.save(client));
    }
}
