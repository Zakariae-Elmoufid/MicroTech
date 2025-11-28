package org.example.microTech.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.microTech.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @NotNull
    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    @Column(name = "sub_total")
    private BigDecimal subTotal;


    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    private BigDecimal discount;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    private BigDecimal tvaAmount    ;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    private BigDecimal totalHT;



    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    private BigDecimal total;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    @Column(name="remaining_amount")
    private BigDecimal remainingAmount;



    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderItem> orderItems;




    @ManyToOne
    @JoinColumn(name = "promo_code_id", nullable = true)
    private PromoCode promoCode;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;


}
