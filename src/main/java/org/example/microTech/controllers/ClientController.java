package org.example.microTech.controllers;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.annotations.Secured;
import org.example.microTech.dto.*;

import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
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


    @Secured(roles = UserRole.ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse> createClient(@Valid @RequestBody ClientCreateDTO dto, HttpSession session) {

        ClientResponseDTO clientResponseDTO = clientService.createClient(dto);
        ApiResponse response =  ApiResponse.builder().message("Client created successfully!")
                .data(clientResponseDTO)
                .status(HttpStatus.CREATED.value()).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @Secured(roles = UserRole.ADMIN)
    @GetMapping
    public ResponseEntity<ApiResponse> getAllClients(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        if (!user.getRole().equals(UserRole.ADMIN)) throw new ForbiddenException("Access denied");
        List<ClientResponseDTO> clients = clientService.getAllClients();
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(clients)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured(roles = {UserRole.ADMIN, UserRole.CLIENT})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id, HttpSession session) {

        ClientResponseDTO client = clientService.getClientById(id);
        ApiResponse response = ApiResponse.builder()
                .message("Successfully retrieved client")
                .status(HttpStatus.OK.value())
                .data(client)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured(roles = UserRole.ADMIN)
    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse>update(@PathVariable Long id, @RequestBody @Valid ClientUpdateDTO dto, HttpSession session) {

        ClientResponseDTO client = clientService.updateClient(id, dto);
        ApiResponse response = ApiResponse.builder()
                .message("Successfully updated clinet")
                .status(HttpStatus.OK.value())
                .data(client)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Secured(roles = UserRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable Long id,HttpSession session) {

        ClientResponseDTO client = clientService.deleteClient(id);
        ApiResponse response = ApiResponse.builder()
                .message("Supplier deleted successfully!")
                .data(client)
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }




}
