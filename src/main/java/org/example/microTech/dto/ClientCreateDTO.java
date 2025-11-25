package org.example.microTech.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.microTech.validation.UniqueEmail;
import org.example.microTech.validation.UniqueUsername;
import org.springframework.web.bind.annotation.RequestBody;

public record ClientCreateDTO(


        @NotBlank(message = " the name of company is required")
        @Size(min = 5, max = 50 ,message = "the name of company must be greate than 5 char and less than 50 ")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
      @UniqueEmail
        String email,

        @NotBlank
        @Size(min = 5, max = 50)
        @UniqueUsername
        String username,


        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password

) {
}
