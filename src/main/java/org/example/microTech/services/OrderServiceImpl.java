package org.example.microTech.services;


import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
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
import java.util.List;
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



    @Transactional
    @Override
    public OrderResponseDTO createOrder(@NotNull OrderRequestDTO dto){
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() ->new ResourceNotFoundException("Client not found"));

        PromoCode promo = discountService.validateAndGetPromo(dto.promoCode());

        Order order = Order.builder()
                .client(client)
                .promoCode(promo)
                .build();


        List<OrderItem> orderItems = createOrderItems(dto.items(), order);

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
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private List<OrderItem> createOrderItems(List<OrderItemRequestDTO> itemDtos, Order order) {
        return itemDtos.stream().map(itemDto -> {
            Product product = productService.chequeQuantityAndDecrementStock(
                    itemDto.productId(),
                    itemDto.quantity()
            );

             OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.quantity())
                    .unitPrice(product.getUnitPrice())
                    .totalLine(product.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.quantity())))
                    .order(order)
                    .build();
            return orderItem;
        }).collect(Collectors.toList());

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
