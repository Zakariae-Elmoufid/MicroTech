package org.example.microTech.services;

import org.example.microTech.dto.OrderRequestDTO;
import org.example.microTech.dto.OrderResponseDTO;

import java.math.BigDecimal;

public interface OrderService {
   public OrderResponseDTO createOrder(OrderRequestDTO dto);
   public OrderResponseDTO  getOrderById(long id);
   public void decrementRemaining(long orderId , BigDecimal amountPaid);

}
