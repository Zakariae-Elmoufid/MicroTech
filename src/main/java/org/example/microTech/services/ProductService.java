package org.example.microTech.services;

import org.example.microTech.dto.ProductDeleteResponseDTO;
import org.example.microTech.dto.ProductRequestDTO;
import org.example.microTech.dto.ProductResponseDTO;
import org.example.microTech.entities.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    public ProductResponseDTO createProduct(ProductRequestDTO dto);
    public ProductResponseDTO updateProduct(long id ,ProductRequestDTO dto);
    public ProductResponseDTO getProductById(long id);
    public List<ProductResponseDTO> getAllProducts();
    public Map<Long, Product> getProductsByIds(List<Long> productIds);
    public ProductDeleteResponseDTO deleteProduct(long id);

}
