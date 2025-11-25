package org.example.microTech.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.microTech.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator  implements ConstraintValidator<UniqueEmail,String> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || email.isBlank()) return true;
        return !clientRepository.existsByEmail(email);
    }
}
