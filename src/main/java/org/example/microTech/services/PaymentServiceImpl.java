package org.example.microTech.services;


import lombok.AllArgsConstructor;
import org.example.microTech.dto.OrderResponseDTO;
import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.example.microTech.entities.Order;
import org.example.microTech.entities.Payment;
import org.example.microTech.enums.PaymentStatus;
import org.example.microTech.enums.PaymentType;
import org.example.microTech.mappers.PaymentMapper;
import org.example.microTech.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements  PaymentService {

    private PaymentRepository paymentRepository;
    private OrderService orderService;
    private PaymentMapper paymentMapper;


    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        OrderResponseDTO order = orderService.getOrderById(request.orderId());
        int lastNumber = paymentRepository.findTopByOrderIdOrderByPaymentNumberDesc(request.orderId())
                .map(Payment::getPaymentNumber)
                .orElse(0);
        int newNumberPayment = lastNumber + 1;

        boolean isNotAchievedLimit = false;
        if(request.type().equals(PaymentType.CASH)){
            isNotAchievedLimit = checkLimitCashPaymentsForDay(order.clientId(),request.amount());
        }
        String reference =  generateReference(request.type(),request.bank(),newNumberPayment )


        Payment payment = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .paymentNumber(newNumberPayment)
                .amount(request.amount())
                .reference(reference)
                .bank(request.bank())
                .dueDate(request.dueDate())
                .paymentMethod(request.type())
                .paymentStatus(PaymentStatus.ON_HOLD)
                .build();
        if(request.type().equals(PaymentType.CASH)){
            payment.setPaymentStatus(PaymentStatus.CASHED);
        }
        return  PaymentResponseDTO(paymentRepository.save(payment));
    }

    private boolean checkLimitCashPaymentsForDay(long clientId,BigDecimal amount){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        BigDecimal totalToday = paymentRepository.sumCashPaymentsForDay(
                PaymentType.CASH,
                startOfDay,
                endOfDay,
                clientId
        );
        BigDecimal newPaymentAmount = amount;
        if(totalToday.add(newPaymentAmount).compareTo(new BigDecimal("20000")) > 0){
            throw new IllegalArgumentException("the client "+ clientId +"can't payer more than 20,000 DH in Cash per day.");
        }
        return true;
    }


    private String generateReference(PaymentType type, String bank,int seq) {
        String prefix = switch (type) {
            case CASH -> "REÇU";
            case CHECK -> "CHQ";
            case BANK_TRANSFER -> "VIR";
        };

        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));


        if(type == PaymentType.CASH){
            return String.format("%s-%s-%03d", prefix, datePart,seq;
        } else {
            return String.format("%s-%s-%03d", prefix, bank.toUpperCase(), seq);
        }
    }



}
