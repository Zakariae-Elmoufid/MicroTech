package org.example.microTech.services;


import lombok.AllArgsConstructor;
import org.example.microTech.dto.ProductRequestDTO;
import org.example.microTech.dto.ProductResponseDTO;
import org.example.microTech.entities.Product;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.ClientMapper;
import org.example.microTech.mappers.ProductMapper;
import org.example.microTech.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper mapper;

    public ProductResponseDTO createProduct(ProductRequestDTO dto){

        Product product = mapper.toEntity(dto);
             Product.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return mapper.toDTO(productRepository.save(product));
    }

    public List<ProductResponseDTO> getAllProducts(){
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return products.stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ProductResponseDTO getProductById(long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(mapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public ProductResponseDTO updateProduct(long id, ProductRequestDTO dto) {
        Product productExiste = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );

       Product product = Product.builder()
               .name(dto.name())
               .stock(dto.stock())
               .unitPrice(dto.unitPrice())
               .build();
        return mapper.toDTO(productRepository.save(product));
    }

}
