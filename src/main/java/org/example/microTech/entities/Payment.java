package org.example.microTech.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.microTech.enums.PaymentType;
import org.example.microTech.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "payment_number")
    private int paymentNumber;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    private BigDecimal amount;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type" ,nullable = false)
    private PaymentType paymentType;


    @NotNull
    private  String reference;

   private String bank;

   private LocalDate dueDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "encais_date")
    private LocalDateTime encaisDate;
}
