package org.example.microTech.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.microTech.enums.UserRole;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @NotBlank(message = "First name is required")
    @Column(name = "userName", nullable = false)
    protected String userName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(nullable = false)
    protected String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    protected UserRole role;




}
