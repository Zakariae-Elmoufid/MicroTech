package org.example.microTech.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @NotNull
    @Positive
    private Integer quantity;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    @Column(name="total_line")
    private BigDecimal totalLine;



    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
