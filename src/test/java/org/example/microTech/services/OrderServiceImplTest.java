package org.example.microTech.services;

import org.example.microTech.dto.*;
import org.example.microTech.entities.*;
import org.example.microTech.enums.OrderStatus;
import org.example.microTech.exceptions.BusinessException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.OrderMapper;
import org.example.microTech.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DiscountService discountService;
    @Mock
    private PromoCodeService promoCodeService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private Client mockClient;
    private Product mockProduct;
    private OrderRequestDTO validOrderRequest;
    private OrderResponseDTO mockOrderResponse;
    private OrderItemRequestDTO itemRequest;

    @BeforeEach
    void setUp() {
        // 1. Set up common Mock Entities/DTOs
        mockClient = Client.builder().id(1L).loyaltyLevel(null).build();
        mockProduct = Product.builder().id(101L).name("Laptop").stock(5).unitPrice(new BigDecimal("1000.00")).build();

        mockOrderResponse = new OrderResponseDTO(
                1L,
                1L,
                OrderStatus.PENDING,
                LocalDateTime.now(),
                new BigDecimal("2000.00"),
                BigDecimal.ZERO, // discount
                new BigDecimal("400.00"), // tvaAmount
                new BigDecimal("2000.00"), // totalHT
                new BigDecimal("2400.00"), // total
                new BigDecimal("2400.00"), // remainingAmount
                Collections.emptyList() // orderItem
        );
        itemRequest = new OrderItemRequestDTO(101L, 2); // 2 units of product 101

        validOrderRequest = new OrderRequestDTO(
                1L, // clientId
                "SUMMER20", // promoCode
                new BigDecimal("0.20"), // tva
                List.of(itemRequest)
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productService.getProductsByIds(anyList())).thenReturn(Map.of(101L, mockProduct));
        when(promoCodeService.validatePromoForClient(anyLong(), anyString())).thenReturn(null); // Assuming null promo for simplicity
        when(discountService.calculateTotalDiscountRate(any(Client.class), any(BigDecimal.class), any())).thenReturn(BigDecimal.ZERO);
        when(orderMapper.toDto(any(Order.class))).thenReturn(mockOrderResponse);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved order
    }



    @Test
    void createOrder_shouldCreateOrder_WhenStockIsSufficient() {

        OrderResponseDTO result = orderServiceImpl.createOrder(validOrderRequest);

        assertNotNull(result);
        assertEquals(mockOrderResponse, result);

        verify(orderRepository, times(1)).save(any(Order.class));
        assertEquals(3, mockProduct.getStock(), "Product stock should be decremented.");
    }



    @Test
    void createOrder_shouldThrowBusinessException_WhenStockInsufficient() {
        // Arrange: Make requested quantity greater than stock
        OrderItemRequestDTO insufficientItemRequest = new OrderItemRequestDTO(101L, 6); // Stock is 5
        OrderRequestDTO insufficientStockRequest = new OrderRequestDTO(
                1L,  "SUMMER20", new BigDecimal("0.20") ,List.of(insufficientItemRequest)
        );

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            orderServiceImpl.createOrder(insufficientStockRequest);
        });

        String expectedDetail = mockProduct.getName() + " (stock=" + mockProduct.getStock() + ")";
        assertTrue(thrown.getMessage().contains("Order rejected: insufficient stock for one or more items."));
        assertTrue(thrown.getMessage().contains(expectedDetail));

        verify(orderRepository, times(1)).save(argThat(order -> order.getOrderStatus() == OrderStatus.REJECTED));
        assertEquals(5, mockProduct.getStock(), "Product stock should NOT be decremented on rejected order.");
    }



    @Test
    void createOrder_shouldThrowNullPointerException_WhenProductIsMissing() {

        when(productService.getProductsByIds(anyList())).thenReturn(Collections.emptyMap()); // No products returned

        assertThrows(NullPointerException.class, () -> {
            orderServiceImpl.createOrder(validOrderRequest);
        }, "Should throw NullPointerException when productMap returns null for an ID.");

    }



    @Test
    void createOrder_shouldThrowResourceNotFoundException_WhenClientIsMissing() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderServiceImpl.createOrder(validOrderRequest);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }
}