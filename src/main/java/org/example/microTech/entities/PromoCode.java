package org.example.microTech.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.microTech.enums.PromoCodeStatus;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "promo_codes")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(
            regexp = "PROMO-[A-Z0-9]{4}",
            message = "Promo code must follow the format PROMO-XXXX (4 uppercase letters or digits)"
    )
    private String promoCode;
    @DecimalMin(value = "0.0", message = "Discount must be positive")
    private BigDecimal discount;
    @Column(name = "start_date")
    private LocalDateTime StartDate;
    @Column(name = "end_date")
    private LocalDateTime EndDate;


    private  Integer  maxUses;

    @Enumerated(EnumType.STRING)
    @Column(name = "promo_code_status")
    private PromoCodeStatus status;
    @CreatedDate
    private LocalDateTime created;


    @OneToMany(mappedBy = "promoCode")
    private List<Order> orders;



}
