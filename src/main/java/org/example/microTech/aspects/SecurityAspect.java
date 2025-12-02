package org.example.microTech.aspects;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.microTech.annotations.Secured;
import org.example.microTech.entities.User;
import org.example.microTech.enums.UserRole;
import org.example.microTech.exceptions.ForbiddenException;
import org.example.microTech.exceptions.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {

    private final HttpSession session;



    @Before("@annotation(secured)")
    public void checkRole( Secured secured) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new UnauthorizedException("You must login");
        }
        UserRole userRole = user.getRole();
        UserRole[] allowedRoles = secured.roles();
        boolean hasAccess = Arrays.stream(allowedRoles)
                .anyMatch(role -> role.equals(userRole));

        if (!hasAccess) {
            throw new ForbiddenException("Access denied");
        }
    }
}
