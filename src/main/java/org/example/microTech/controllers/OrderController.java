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
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @Secured(roles = UserRole.ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody OrderRequestDTO dto, HttpSession session){
        OrderResponseDTO orderResponseDTO = orderService.createOrder(dto);
        ApiResponse response = ApiResponse.builder().message("Order created successfully")
                .data(orderResponseDTO)
                .status(HttpStatus.CREATED.value()).build();
        return ResponseEntity.ok(response);
    }

    @Secured(roles = UserRole.ADMIN)
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmOrder(@PathVariable long id,HttpSession session){
        orderService.confirmOrder(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Order confirmed successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Secured(roles = UserRole.ADMIN)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable long id, HttpSession session){
        orderService.cancelOrder(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Order canceled successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Secured(roles = UserRole.ADMIN)
    @GetMapping
    public Page<OrderHistoryDTO> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session
    ) {

        return orderService.getOrders(page, size);
    }






}
