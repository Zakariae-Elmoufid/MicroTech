package org.example.microTech.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.OrderRequestDTO;
import org.example.microTech.dto.OrderResponseDTO;
import org.example.microTech.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody OrderRequestDTO dto){
        OrderResponseDTO orderResponseDTO = orderService.createOrder(dto);
        ApiResponse response = ApiResponse.builder().message("Order created successfully")
                .data(orderResponseDTO)
                .status(HttpStatus.CREATED.value()).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmOrder(@PathVariable long id){
        orderService.confirmOrder(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Order confirmed successfully")
                .build();
        return ResponseEntity.ok(response);
    }



}
