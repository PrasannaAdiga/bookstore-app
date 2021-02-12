package com.bookstore.learning.adapter.web.v1.impl;

import com.bookstore.learning.adapter.web.v1.IPaymentMethodController;
import com.bookstore.learning.adapter.web.v1.request.CreatePaymentMethodRequest;
import com.bookstore.learning.adapter.web.v1.response.PaymentMethodResponse;
import com.bookstore.learning.application.port.in.IPaymentMethodCommandService;
import com.bookstore.learning.application.port.in.IPaymentMethodQueryService;
import com.bookstore.learning.domain.Card;
import com.bookstore.learning.domain.PaymentMethodType;
import com.bookstore.learning.infrastructure.annotation.WebAdapter;
import com.bookstore.learning.infrastructure.constant.PaymentServiceConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    /*============================================ Command Section ===============================================*/

    @Override
    public ResponseEntity<?> createPaymentMethod(CreatePaymentMethodRequest createPaymentMethodRequest, Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(PaymentServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        Card card = Card.builder()
                .firstName(createPaymentMethodRequest.getFirstName())
                .lastName(createPaymentMethodRequest.getLastName())
                .cardNumber(createPaymentMethodRequest.getCardNumber())
                .last4Digits(createPaymentMethodRequest.getLast4Digits())
                .expirationMonth(createPaymentMethodRequest.getExpirationMonth())
                .expirationYear(createPaymentMethodRequest.getExpirationYear())
                .cvv(createPaymentMethodRequest.getCvv())
                .build();
        String paymentMethodId = paymentMethodCommandService.createPaymentMethod(card, loggedInUserEmail);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(paymentMethodId).toUri();
        return ResponseEntity.created(location).build();
    }

    /*============================================ Query Section ================================================*/

    @Override
    public ResponseEntity<List<PaymentMethodResponse>> getAllPaymentMethodOfUser(@AuthenticationPrincipal Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(PaymentServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        List<PaymentMethodType> paymentMethodTypes = paymentMethodQueryService.getAllPaymentMethodOfUser(loggedInUserEmail);
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
    public ResponseEntity<PaymentMethodResponse> getPaymentMethodOfUserById(String paymentMethodId, @AuthenticationPrincipal Jwt principal) {
        String loggedInUserEmail = principal.getClaimAsString(PaymentServiceConstant.ACCESS_TOKEN_EMAIL_FIELD_NAME);
        PaymentMethodType paymentMethodType = paymentMethodQueryService.getPaymentMethodOfUserById(paymentMethodId, loggedInUserEmail);
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
