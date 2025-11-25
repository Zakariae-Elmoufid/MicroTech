package org.example.microTech.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.microTech.dto.ApiResponse;
import org.example.microTech.dto.ProductRequestDTO;
import org.example.microTech.dto.ProductResponseDTO;
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
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO product = productService.createProduct(dto);

        ApiResponse response =  ApiResponse.builder().message("Product created successfully!")
                .data(product)
                .status(HttpStatus.CREATED.value()).build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
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
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO product = productService.updateProduct(id, dto);

        ApiResponse response = ApiResponse.builder()
                .message("Product update ")
                .data(product)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }



}
