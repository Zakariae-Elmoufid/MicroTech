package org.example.microTech.services;


import org.example.microTech.dto.ClientOrderStatsDTO;
import org.example.microTech.dto.OrderHistoryDTO;
import org.example.microTech.mappers.OrderMapper;
import org.example.microTech.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientOrderServiceImpl implements  ClientOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    public ClientOrderStatsDTO getClientStats(Long clientId) {

        int totalOrdersPending = orderRepository.countClientOrdersPending(clientId);
        int totalOrdersConfirmed = orderRepository.countClientOrdersConfirmed(clientId);
        BigDecimal confirmedTotal = orderRepository.sumClientConfirmedOrders(clientId);
        LocalDateTime first = orderRepository.firstClientOrderDate(clientId);
        LocalDateTime last = orderRepository.lastClientOrderDate(clientId);

        List<OrderHistoryDTO> history = orderRepository.findAllByClient(clientId)
                .stream()
                .map(orderMapper::toHistoryDTO)
                .toList();

        ClientOrderStatsDTO dto =  ClientOrderStatsDTO.builder()
                .totalOrdersPending(totalOrdersPending)
                .totalOrdersConfirmed(totalOrdersConfirmed)
                .cumulativeAmount(confirmedTotal)
                .firstOrderDate(first)
                .lastOrderDate(last)
                .orderHistory(history)
                .build();
        return dto;
    }
}
