package org.example.microTech.controllers;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.*;
import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
import org.example.microTech.repositories.ProductRepository;
import org.example.microTech.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductRequestDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        if (!user.getRole().equals(UserRole.ADMIN)) throw new ForbiddenException("Access denied");
        ProductResponseDTO product = productService.createProduct(dto);

        ApiResponse response =  ApiResponse.builder().message("Product created successfully!")
                .data(product)
                .status(HttpStatus.CREATED.value()).build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        if (!user.getRole().equals(UserRole.ADMIN)) throw new ForbiddenException("Access denied");
        List<ProductResponseDTO> products = productService.getAllProducts();
        ApiResponse response =  ApiResponse.builder().message("All products")
                .data(products)
                .status(HttpStatus.CREATED.value()).build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);

        ApiResponse response = ApiResponse.builder()
                .message("Product found")
                .data(product)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        if (!user.getRole().equals(UserRole.ADMIN)) throw new ForbiddenException("Access denied");
        ProductResponseDTO product = productService.updateProduct(id, dto);

        ApiResponse response = ApiResponse.builder()
                .message("Product update ")
                .data(product)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id,HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("You must login");
        if (!user.getRole().equals(UserRole.ADMIN)) throw new ForbiddenException("Access denied");
        ProductDeleteResponseDTO client = productService.deleteProduct(id);
        ApiResponse response = ApiResponse.builder()
                .message("Supplier deleted successfully!")
                .data(client)
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }



}
