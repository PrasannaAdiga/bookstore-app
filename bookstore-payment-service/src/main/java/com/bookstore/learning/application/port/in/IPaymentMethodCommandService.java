package com.bookstore.learning.application.port.in;

import com.bookstore.learning.domain.Card;

public interface IPaymentMethodCommandService {
    String createPaymentMethod(Card card);
}
