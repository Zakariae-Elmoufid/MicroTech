package org.example.microTech.services;


import lombok.AllArgsConstructor;
import org.example.microTech.dto.OrderResponseDTO;
import org.example.microTech.dto.PaymentRequestDTO;
import org.example.microTech.dto.PaymentResponseDTO;
import org.example.microTech.entities.Order;
import org.example.microTech.entities.Payment;
import org.example.microTech.enums.PaymentStatus;
import org.example.microTech.enums.PaymentType;
import org.example.microTech.exceptions.ResourceNotFoundException;
import org.example.microTech.mappers.PaymentMapper;
import org.example.microTech.repositories.OrderRepository;
import org.example.microTech.repositories.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements  PaymentService {

    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private PaymentMapper paymentMapper;


    public PaymentResponseDTO createPayment(long orderId,PaymentRequestDTO request) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("the order "+ orderId + " not found")
        );


        int lastNumber = paymentRepository.findTopByOrderIdOrderByPaymentNumberDesc(orderId)
                .map(Payment::getPaymentNumber)
                .orElse(0);
        int newNumberPayment = lastNumber + 1;

        boolean isNotAchievedLimit = false;
        if(request.type().equals(PaymentType.CASH)){
            isNotAchievedLimit = checkLimitCashPaymentsForDay(order.getClient().getId(),request.amount());
        }
        String reference =  generateReference(request.type(),request.bank(),newNumberPayment );

        Payment payment = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .order(order)
                .paymentNumber(newNumberPayment)
                .amount(request.amount())
                .reference(reference)
                .bank(request.bank())
                .dueDate(request.dueDate())
                .paymentType(request.type())
                .paymentStatus(PaymentStatus.ON_HOLD)
                .build();
        orderService.decrementRemaining(orderId , request.amount() );
        if(request.type().equals(PaymentType.CASH)){
            payment.setPaymentStatus(PaymentStatus.CASHED);
        }
        return  paymentMapper.toDTO(paymentRepository.save(payment));
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
        if(totalToday.add(amount).compareTo(new BigDecimal("20000")) > 0){
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
            return String.format("%s-%s-%03d", prefix, datePart,seq);
        } else {
            return String.format("%s-%s-%03d", prefix, bank.toUpperCase(), seq);
        }
    }


    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return paymentMapper.toDTO(payment);
    }

    public List<PaymentResponseDTO> getPaymentsByOrderId(Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);

        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("No payments found for order id: " + orderId);
        }

        return payments.stream()
                .map(paymentMapper::toDTO)
                .toList();
    }

    public Page<PaymentResponseDTO> getAllPayments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
        return paymentRepository.findAllPayments(pageable)
                .map(paymentMapper::toDTO);
    }



}
