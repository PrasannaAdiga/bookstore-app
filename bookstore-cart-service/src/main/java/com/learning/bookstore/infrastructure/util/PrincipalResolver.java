package com.learning.bookstore.infrastructure.util;

import com.learning.bookstore.infrastructure.constant.CartServiceConstant;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class PrincipalResolver {
    public String getCurrentLoggedInUserMail() {
        return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getClaimAsString(CartServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
    }
}
