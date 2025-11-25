package org.example.microTech.controllers;


import jakarta.validation.Valid;
import org.example.microTech.dto.ApiResponse;

import org.example.microTech.dto.ClientCreateDTO;
import org.example.microTech.dto.ClientResponseDTO;
import org.example.microTech.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping("/api/clients")
    public ResponseEntity<ApiResponse> createClient(@Valid @RequestBody ClientCreateDTO dto){
        ClientResponseDTO clientResponseDTO = clientService.createClient(dto);
        ApiResponse response =  ApiResponse.builder().message("Client created successfully!")
                .data(clientResponseDTO)
                .status(HttpStatus.CREATED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


}
