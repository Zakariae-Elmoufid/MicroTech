package org.example.microTech.controllers;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.annotations.Secured;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
import org.example.microTech.services.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @Secured(roles = UserRole.ADMIN)
    @PostMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> createPayment(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequestDTO dto,
            HttpSession session
    ){

        PaymentResponseDTO payment = paymentService.createPayment(orderId, dto);
        ApiResponse response = ApiResponse.builder().message("Payment pass successfuly")
                .data(payment)
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @Secured(roles = {UserRole.ADMIN, UserRole.CLIENT})
    @GetMapping("/{id}")
    public PaymentResponseDTO getPaymentById(@PathVariable Long id,HttpSession session) {

        return paymentService.getPaymentById(id);
    }

    @Secured(roles = {UserRole.ADMIN , UserRole.CLIENT})
    @GetMapping("/order/{orderId}")
    public List<PaymentResponseDTO> getPaymentsByOrder(@PathVariable Long orderId, HttpSession session) {

        return paymentService.getPaymentsByOrderId(orderId);
    }

    @Secured(roles = UserRole.ADMIN)
    @GetMapping
    public Page<PaymentResponseDTO> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session
    ) {

        return paymentService.getAllPayments(page, size);
    }

}
