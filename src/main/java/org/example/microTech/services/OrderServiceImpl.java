package org.example.microTech.services;



import org.example.microTech.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.microTech.entities.*;
import org.example.microTech.enums.OrderStatus;
import org.example.microTech.exceptions.BusinessException;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.OrderMapper;
import org.example.microTech.repositories.ClientRepository;
import org.example.microTech.repositories.OrderItemsRepository;
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.PromoCodeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.microTech.enums.CustomerTier.*;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ProductService productService;
    private final ClientRepository clientRepository;
    private final DiscountService discountService;
    private final PromoCodeService promoCodeService;
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

        PromoCode promo = promoCodeService.validatePromoForClient(dto.clientId(), dto.promoCode());

        System.out.println("Code Promo : "+ promo);
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

        BigDecimal subTotal =orderItems.stream().map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
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

    @Transactional
    public void confirmOrder(long id) {

        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order " + id + " not found")
        );

        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessException("Order " + id + " is already confirmed");
        }

        if (order.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("Remaining amount is greater than 0. The order is not fully paid.");
        }

        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        recalculateLoyaltyLevel(order.getClient());

    }



    public void   recalculateLoyaltyLevel(Client client){
        List<Order> orders = orderRepository.findByClientIdAndOrderStatus(client.getId(),OrderStatus.CONFIRMED);
        BigDecimal subTotal = orders.stream().map(order -> order.getSubTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
        int orderSize = orders.size();


        if (orderSize >= 20 || subTotal.compareTo(new BigDecimal("15000.00")) >= 0) {
            client.setLoyaltyLevel(PLATINUM);
        } else if (orderSize >= 10 || subTotal.compareTo(new BigDecimal("5000.00")) >= 0) {
            client.setLoyaltyLevel(GOLD);

        } else if (orderSize >= 3 || subTotal.compareTo(new BigDecimal("1000.00")) >= 0) {
            client.setLoyaltyLevel(SILVER);

        } else {
            client.setLoyaltyLevel(BASIC);
        }
        clientRepository.save(client);
    }

    public Page<OrderHistoryDTO> getOrders(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

        return orderRepository.findAllOrdersAdmin(pageable)
                .map(orderMapper::toHistoryDTO);
    }

    public void cancelOrder(long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order " + id + " not found")
        );

        if(order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Order " + id + " is not pending \n satatus :" +order.getOrderStatus() );
        }

        productService.backProductInStock(order.getOrderItems());
        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }




}
