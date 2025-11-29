package org.example.microTech.services;


import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.microTech.dto.OrderItemRequestDTO;
import org.example.microTech.dto.OrderRequestDTO;
import org.example.microTech.dto.OrderResponseDTO;
import org.example.microTech.entities.*;
import org.example.microTech.enums.CustomerTier;
import org.example.microTech.enums.OrderStatus;
import org.example.microTech.enums.PromoCodeStatus;
import org.example.microTech.exceptions.BusinessException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.OrderMapper;
import org.example.microTech.repositories.ClientRepository;
import org.example.microTech.repositories.OrderItemsRepository;
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.PromoCodeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.microTech.enums.CustomerTier.*;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ProductService productService;
    private final ClientRepository clientRepository;
    private final DiscountService discountService;
    private  final OrderItemsRepository orderItemsRepository;

    private final OrderMapper orderMapper;



    @Transactional(noRollbackFor = BusinessException.class)
    @Override
    public OrderResponseDTO createOrder(@NotNull OrderRequestDTO dto){
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() ->new ResourceNotFoundException("Client not found"));

        Map<Long, Product> productMap = productService.getProductsByIds(
                dto.items().stream().map(OrderItemRequestDTO::productId).toList()
        );

        PromoCode promo = discountService.validateAndGetPromo(dto.promoCode());

        Order order = Order.builder()
                .client(client)
                .promoCode(promo)
                .build();
        List<Product>  insufficientProducts  = getInsufficientProducts(dto.items(), productMap);
        boolean insufficient = !insufficientProducts.isEmpty();

        if (insufficient) {
            order.setOrderStatus(OrderStatus.REJECTED);
        } else {
            order.setOrderStatus(OrderStatus.PENDING);
        }


        List<OrderItem> orderItems = createOrderItems(dto.items(), order,productMap,insufficient);

        BigDecimal subTotal = dto.items().stream().map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("subTotal: " + subTotal);
        order.setSubTotal(subTotal);


        BigDecimal totalDiscountRate = discountService.calculateTotalDiscountRate(
                order.getClient(),
                subTotal,
                order.getPromoCode()
        );

        BigDecimal discountAmount = subTotal.multiply(totalDiscountRate);
        order.setDiscount(discountAmount);
        BigDecimal totalHTdiscounted = subTotal.subtract(discountAmount);
        order.setTotalHT(totalHTdiscounted);

        BigDecimal tva = (dto.tva() == null)
                ? new BigDecimal("0.20")
                : dto.tva();

        BigDecimal tvaAmount  = totalHTdiscounted.multiply(tva);
        order.setTvaAmount(tvaAmount);

        BigDecimal totalTTC = totalHTdiscounted.add(tvaAmount);

        order.setTotal(totalTTC);
        order.setOrderItems(orderItems);
        order.setRemainingAmount(totalTTC);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        if (insufficient) {
            String details = insufficientProducts.stream()
                    .map(p -> p.getName() + " (stock=" + p.getStock() + ")")
                    .reduce((a,b) -> a + ", " + b)
                    .orElse("Unknown products");
            throw new BusinessException("Order rejected: insufficient stock for one or more items." + details);
        }

        return orderMapper.toDto(order);
    }

    private List<Product> getInsufficientProducts(List<OrderItemRequestDTO> items, Map<Long, Product> productMap) {
        List<Product> insufficient = new ArrayList<>();

        for (OrderItemRequestDTO dto : items) {
            Product p = productMap.get(dto.productId());
            if (p.getStock() < dto.quantity()) {
                insufficient.add(p);
            }
        }
        return insufficient;
    }


    private List<OrderItem> createOrderItems(List<OrderItemRequestDTO> itemDtos, Order order ,Map<Long, Product> productMap,boolean insufficient ) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : itemDtos) {
            Product product = productMap.get(itemDTO.productId());

            boolean insufficientForThisItem = product.getStock() < itemDTO.quantity();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
            item.setUnitPrice(product.getUnitPrice());
            item.setTotalLine(product.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.quantity())));
            item.setInsufficientStock(insufficientForThisItem);

            orderItems.add(item);

            if (!insufficient && !insufficientForThisItem) {
                product.setStock(product.getStock() - itemDTO.quantity());
            }
        }
        return orderItems;
    }

    public void decrementRemaining(long orderId , BigDecimal amountPaid){
        Order order =  orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("the order "+ orderId + " not found")
        );

        if(amountPaid.compareTo(order.getRemainingAmount()) > 0){
            throw new BusinessException("amount paid is greater  than remaining amount");
        }
        BigDecimal newRemainingAmount = order.getRemainingAmount().subtract(amountPaid);
        order.setRemainingAmount(newRemainingAmount);
    }


    public OrderResponseDTO  getOrderById(long id){
        Order order=  orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("the order "+ id + " not found")
        );
        return orderMapper.toDto(order);
    }

    public  void  confirmOrder(long id){
        Order order=  orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("the order "+ id + " not found")
        );


        if(order.getOrderStatus().equals(OrderStatus.CONFIRMED)){
            throw new BusinessException("the order "+ id + " is already in complete");
        }

        if(order.getRemainingAmount().compareTo(new BigDecimal("0.00")) > 0){
            throw new BusinessException("remaining amount  is greater  than 0 , it's not complete pay ");
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

    }



    public void  RecalculateLoyaltyLevel(Client client){
        List<Order> orders = orderRepository.findByClientId(client.getId());
        BigDecimal totalOrder = orders.stream().map(order -> order.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
        int orderSize = orders.size();


        if (orderSize >= 20 || totalOrder.compareTo(new BigDecimal("15000.00")) >= 0) {
            client.setLoyaltyLevel(PLATINUM);

        } else if (orderSize >= 10 || totalOrder.compareTo(new BigDecimal("5000.00")) >= 0) {
            client.setLoyaltyLevel(GOLD);

        } else if (orderSize >= 3 || totalOrder.compareTo(new BigDecimal("1000.00")) >= 0) {
            client.setLoyaltyLevel(SILVER);

        } else {
            client.setLoyaltyLevel(BASIC);
        }
        clientRepository.save(client);
    }
}
