package com.learning.bookstore.application.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardResponse {
    private String paymentMethodId;
    private String cardBrand;
    private String last4Digits;
}
