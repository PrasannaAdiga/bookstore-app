package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IPaymentMethodController;
import com.bookstore.learning.adapter.web.v1.request.CreatePaymentMethodRequest;
import com.bookstore.learning.adapter.web.v1.response.PaymentMethodResponse;
import com.bookstore.learning.application.port.in.IPaymentMethodCommandService;
import com.bookstore.learning.application.port.in.IPaymentMethodQueryService;
import com.bookstore.learning.domain.Card;
import com.bookstore.learning.domain.PaymentMethodType;
import com.bookstore.learning.infrastructure.annotation.WebAdapter;
import com.bookstore.learning.infrastructure.util.PrincipalResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@WebAdapter
@RequiredArgsConstructor
public class PaymentMethodController implements IPaymentMethodController {
    private final IPaymentMethodCommandService paymentMethodCommandService;
    private final IPaymentMethodQueryService paymentMethodQueryService;
    private final PrincipalResolver principalResolver;

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createPaymentMethod(CreatePaymentMethodRequest createPaymentMethodRequest) {
        Card card = Card.builder()
                .firstName(createPaymentMethodRequest.getFirstName())
                .lastName(createPaymentMethodRequest.getLastName())
                .cardNumber(createPaymentMethodRequest.getCardNumber())
                .last4Digits(createPaymentMethodRequest.getLast4Digits())
                .expirationMonth(createPaymentMethodRequest.getExpirationMonth())
                .expirationYear(createPaymentMethodRequest.getExpirationYear())
                .cvc(createPaymentMethodRequest.getCvv())
                .build();
        String paymentMethodId = paymentMethodCommandService.createPaymentMethod(card);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(paymentMethodId).toUri();
        return ResponseEntity.created(location).build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<List<PaymentMethodResponse>> getAllPaymentMethodOfUser() {
        List<PaymentMethodType> paymentMethodTypes = paymentMethodQueryService.getAllPaymentMethodOfUser();
        List<PaymentMethodResponse> paymentMethodResponses = new ArrayList<>();
        paymentMethodTypes.forEach(paymentMethodType -> {
            paymentMethodResponses.add(PaymentMethodResponse.builder()
                    .cardType(paymentMethodType.getCardType())
                    .cardCountry(paymentMethodType.getCardCountry())
                    .cardLast4Digits(paymentMethodType.getCardLast4Digits())
                    .paymentMethodId(paymentMethodType.getId())
                    .cardExpirationMonth(paymentMethodType.getCardExpirationMonth())
                    .cardExpirationYear(paymentMethodType.getCardExpirationYear())
                    .build());
        });
        return ResponseEntity.ok().body(paymentMethodResponses);
    }

    @Override
    public ResponseEntity<PaymentMethodResponse> getPaymentMethodOfUserById(String paymentMethodId) {
        PaymentMethodType paymentMethodType = paymentMethodQueryService.getPaymentMethodOfUserById(paymentMethodId);
        PaymentMethodResponse paymentMethodResponse = PaymentMethodResponse.builder()
                .cardType(paymentMethodType.getCardType())
                .cardCountry(paymentMethodType.getCardCountry())
                .cardLast4Digits(paymentMethodType.getCardLast4Digits())
                .paymentMethodId(paymentMethodType.getId())
                .cardExpirationMonth(paymentMethodType.getCardExpirationMonth())
                .cardExpirationYear(paymentMethodType.getCardExpirationYear())
                .build();
        return ResponseEntity.ok().body(paymentMethodResponse);
    }

}
