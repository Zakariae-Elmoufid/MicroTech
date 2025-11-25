package org.example.microTech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.microTech.validation.UniqueEmail;

public record ClientUpdateDTO(
        @NotBlank(message = " the name of company is required")
        @Size(min = 5, max = 50 ,message = "the name of company must be greate than 5 char and less than 50 ")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email
) {
}
