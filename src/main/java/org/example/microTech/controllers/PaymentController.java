package org.example.microTech.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.example.microTech.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/orders")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/payments")
    public ResponseEntity<ApiResponse> createPayment(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequestDTO dto
    ){
        PaymentResponseDTO payment = paymentService.createPayment(orderId, dto);
        ApiResponse response = ApiResponse.builder().message("Payment pass successfuly")
                .data(payment)
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
