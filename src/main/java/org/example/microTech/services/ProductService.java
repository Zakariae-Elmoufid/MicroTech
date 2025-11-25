package org.example.microTech.services;

import org.example.microTech.dto.ProductRequestDTO;
import org.example.microTech.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    public ProductResponseDTO createProduct(ProductRequestDTO dto);
    public ProductResponseDTO updateProduct(long id ,ProductRequestDTO dto);
    public ProductResponseDTO getProductById(long id);
    public List<ProductResponseDTO> getAllProducts();
}
