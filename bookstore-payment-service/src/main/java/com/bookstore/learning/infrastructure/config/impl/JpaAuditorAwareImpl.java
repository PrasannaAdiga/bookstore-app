package com.bookstore.learning.infrastructure.config.impl;

import com.bookstore.learning.infrastructure.constant.PaymentServiceConstant;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class JpaAuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){
        return Optional.of(((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaimAsString(PaymentServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME));
    }
}
