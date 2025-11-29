package org.example.microTech.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
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

    @PostMapping("/orders/{orderId}")
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

    @GetMapping("/{id}")
    public PaymentResponseDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentResponseDTO> getPaymentsByOrder(@PathVariable Long orderId) {
        return paymentService.getPaymentsByOrderId(orderId);
    }

    @GetMapping
    public Page<PaymentResponseDTO> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return paymentService.getAllPayments(page, size);
    }

}
