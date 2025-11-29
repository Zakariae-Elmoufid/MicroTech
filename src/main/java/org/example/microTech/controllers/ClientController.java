package org.example.microTech.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.*;

import org.example.microTech.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;


    @PostMapping
    public ResponseEntity<ApiResponse> createClient(@Valid @RequestBody ClientCreateDTO dto){
        ClientResponseDTO clientResponseDTO = clientService.createClient(dto);
        ApiResponse response =  ApiResponse.builder().message("Client created successfully!")
                .data(clientResponseDTO)
                .status(HttpStatus.CREATED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllClients(){
        List<ClientResponseDTO> clients = clientService.getAllClients();
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(clients)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.getClientById(id);
        ApiResponse response = ApiResponse.builder()
                .message("Successfully retrieved client")
                .status(HttpStatus.OK.value())
                .data(client)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse>update(@PathVariable Long id, @RequestBody @Valid ClientUpdateDTO dto) {

        ClientResponseDTO client = clientService.updateClient(id, dto);
        ApiResponse response = ApiResponse.builder()
                .message("Successfully updated clinet")
                .status(HttpStatus.OK.value())
                .data(client)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable Long id) {
        ClientResponseDTO client = clientService.deleteClient(id);
        ApiResponse response = ApiResponse.builder()
                .message("Supplier deleted successfully!")
                .data(client)
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }




}
