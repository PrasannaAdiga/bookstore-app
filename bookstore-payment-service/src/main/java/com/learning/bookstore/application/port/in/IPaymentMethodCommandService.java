package com.learning.bookstore.application.port.in;

import com.learning.bookstore.domain.Card;

public interface IPaymentMethodCommandService {
    String createPaymentMethod(Card card);
}
