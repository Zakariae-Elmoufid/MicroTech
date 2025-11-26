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
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.PromoCodeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.microTech.enums.CustomerTier.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ProductService productService;
    private final ClientRepository clientRepository;

    private final OrderMapper orderMapper;


    @Transactional
    public OrderResponseDTO createOrder( OrderRequestDTO dto){
        Order order =  new Order();
        PromoCode promo = null;
        if(dto.promoCode()!=null){
            promo = promoCodeRepository.findByPromoCodeAndStatus(dto.promoCode(), PromoCodeStatus.ACTIVE).orElseThrow(
                    () ->new ResourceNotFoundException("Promo code not found")
            );
        }
        order.setPromoCode(promo);

        for(OrderItemRequestDTO item : dto.items()){
          Product product  = productService.chequeQuantityAndDecrementStock(item.productId(),item.qauntity());
          if(product==null){
              throw new BusinessException("Product "+ item.productId()+" not has stock quantity enough");
          }

          OrderItem orderItem = OrderItem.builder()
                  .product(product)
                  .quantity(item.qauntity())
                  .unitPrice(product.getUnitPrice())
                  .totalLine(product.getUnitPrice().multiply(BigDecimal.valueOf(item.qauntity())))
                  .order(order)
                  .build();
        }


        BigDecimal subTotal = dto.items().stream().map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.qauntity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() ->new ResourceNotFoundException("Client not found"));

        order.setClient(client);


            BigDecimal loyaltyDiscount   = BigDecimal.ZERO;
        switch (client.getLoyaltyLevel()) {
            case SILVER:
                if (subTotal.compareTo(new BigDecimal("500.00")) >= 0) {
                    loyaltyDiscount  = new BigDecimal("0.05"); // 5%
                }
                break;

            case GOLD:
                if (subTotal.compareTo(new BigDecimal("800.00")) >= 0) {
                    loyaltyDiscount  = new BigDecimal("0.10");
                }
                break;

            case PLATINUM:
                if (subTotal.compareTo(new BigDecimal("1200.00")) >= 0) {
                    loyaltyDiscount  = new BigDecimal("0.15");
                }
                break;

            default:
                loyaltyDiscount = BigDecimal.ZERO;
        }

        BigDecimal promoDiscount = (promo != null) ? promo.getDiscount() : BigDecimal.ZERO;

        BigDecimal totalDiscountRate = loyaltyDiscount.add(promoDiscount);

        BigDecimal discountAmount = subTotal.multiply(totalDiscountRate);
        BigDecimal totalHTdiscounted = subTotal.subtract(discountAmount);

        BigDecimal tva = (dto.tva() == null)
                ? new BigDecimal("0.20")
                : dto.tva();

        BigDecimal tvaAmount  = totalHTdiscounted.multiply(tva);
        BigDecimal totalTTC = totalHTdiscounted.add(tvaAmount);

        order.setSubTotal(subTotal);
        order.setDiscount(totalHTdiscounted);
        order.setTva(tvaAmount);
        order.setTotal(totalTTC);
        order.setRemainingAmount(totalTTC);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
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
