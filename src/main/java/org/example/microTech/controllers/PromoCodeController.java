package org.example.microTech.controllers;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.PromoCodeRequestDTO;
import org.example.microTech.dto.PromoCodeResponseDTO;
import org.example.microTech.entities.PromoCode;
import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
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
    ResponseEntity<ApiResponse> createPromoCode(@RequestBody @Valid PromoCodeRequestDTO request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        if (!user.getRole().equals(UserRole.ADMIN)) throw new ForbiddenException("Access denied");
        PromoCodeResponseDTO codePromo = promoCodeService.createPromo( request);
        ApiResponse response = ApiResponse.builder()
                .data(codePromo)
                .message("code promo created successfully")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
