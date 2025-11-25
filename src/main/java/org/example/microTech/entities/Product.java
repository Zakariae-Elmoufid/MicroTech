package org.example.microTech.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "First name is required")
    private String name;

    @DecimalMin(value = "0.0", message = "The value must be greater than or equal to 0")
    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @Min(value = 0, message = "The value must be greater than or equal to 0")
    private Integer stock;

    private boolean active;

    private LocalDateTime createdAt;

}
