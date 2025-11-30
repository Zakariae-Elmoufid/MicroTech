package org.example.microTech.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.PromoCodeRequestDTO;
import org.example.microTech.dto.PromoCodeResponseDTO;
import org.example.microTech.entities.PromoCode;
import org.example.microTech.repositories.PromoCodeRepository;
import org.example.microTech.services.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/promo-code")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;


    @PostMapping
    ResponseEntity<ApiResponse> createPromoCode(@RequestBody @Valid PromoCodeRequestDTO request) {
        PromoCodeResponseDTO codePromo = promoCodeService.createPromo( request);
        ApiResponse response = ApiResponse.builder()
                .data(codePromo)
                .message("code promo created successfully")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
