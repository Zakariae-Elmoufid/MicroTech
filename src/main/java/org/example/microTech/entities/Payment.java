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
    private int paymentNumber;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    private BigDecimal amount;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method" ,nullable = false)
    private PaymentType paymentMethod;



    private  String reference;

   private String bank;            // optional, validated in service for CHÈQUE / VIREMENT
   private LocalDate dueDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    private LocalDateTime encaisDate;
}
