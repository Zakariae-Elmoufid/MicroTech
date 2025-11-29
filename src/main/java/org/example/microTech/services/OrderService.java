package org.example.microTech.services;

import org.example.microTech.dto.ClientOrderStatsDTO;
import org.example.microTech.dto.OrderHistoryDTO;
import org.example.microTech.dto.OrderRequestDTO;
import org.example.microTech.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface OrderService {
   public OrderResponseDTO createOrder(OrderRequestDTO dto);
   public  void  confirmOrder(long id);
   public OrderResponseDTO  getOrderById(long id);
   public void decrementRemaining(long orderId , BigDecimal amountPaid);

   public Page<OrderHistoryDTO> getOrders(int page, int size);
}
