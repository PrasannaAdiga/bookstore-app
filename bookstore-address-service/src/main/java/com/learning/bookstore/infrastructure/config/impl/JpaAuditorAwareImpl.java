package com.learning.bookstore.infrastructure.config.impl;

import com.learning.bookstore.infrastructure.constant.AddressServiceConstant;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class JpaAuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){
        return Optional.of(((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaimAsString(AddressServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME));
    }
}
