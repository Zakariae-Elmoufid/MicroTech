package org.example.microTech.services;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microTech.dto.ProductDeleteResponseDTO;
import org.example.microTech.dto.ProductRequestDTO;
import org.example.microTech.dto.ProductResponseDTO;
import org.example.microTech.entities.Client;
import org.example.microTech.entities.Order;
import org.example.microTech.entities.OrderItem;
import org.example.microTech.entities.Product;
import org.example.microTech.enums.OrderStatus;
import org.example.microTech.exceptions.BusinessException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.ClientMapper;
import org.example.microTech.mappers.ProductMapper;
import org.example.microTech.repositories.ClientRepository;
import org.example.microTech.repositories.OrderItemsRepository;
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemsRepository orderItemRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

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

    public Map<Long, Product>  getProductsByIds(List<Long> productIds){
        List<Product> products = productRepository.findAllById(productIds);
        if(products.isEmpty()){
            throw new ResourceNotFoundException("Product not found");
        }
        return products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }






    public ProductDeleteResponseDTO deleteProduct(long id) {
        long pendingOrders = orderItemRepository.countPendingOrdersByProductId(id);
        System.out.println("pendingOrders: " + pendingOrders);
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        if(pendingOrders > 0) {
            product.setActive(false);
            productRepository.save(product);
            return new ProductDeleteResponseDTO(product.getName(), "SOFT_DELETED because there are "+ pendingOrders +"  pending orders");

        }else{
            productRepository.delete(product);
            return new ProductDeleteResponseDTO(product.getName(), "HARD_DELETED");

        }
    }
    public void  backProductInStock(List<OrderItem> items){
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product " + item.getProduct().getId() + " not found")
                    );

            product.setStock(product.getStock() + item.getQuantity());

            productRepository.save(product);
        }

    }



    public List<ProductResponseDTO> getProductAchterParClient(long clientId){
//        Client clinet = clientRepository.findById(clientId).orElseThrow(
//                () -> new ResourceNotFoundException("client not found")
//        );

        List<Order> orders = orderRepository.findAllByClient(clientId);

        return  orders.stream().filter(o-> o.getOrderStatus() == OrderStatus.CONFIRMED)
                .flatMap(o -> o.getOrderItems().stream())
                .map(OrderItem::getProduct)
                .map(mapper::toDTO)
                .toList();

    }


}
