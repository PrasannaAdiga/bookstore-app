package com.bookstore.learning.application.service;

import com.bookstore.learning.application.exception.ResourceNotFoundException;
import com.bookstore.learning.application.exception.StripeAPICallException;
import com.bookstore.learning.application.port.in.IPaymentMethodQueryService;
import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.domain.PaymentMethodType;
import com.bookstore.learning.domain.UserPaymentCustomer;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class PaymentMethodQueryService implements IPaymentMethodQueryService {
    private final IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider;

    @Override
    public List<PaymentMethodType> getAllPaymentMethodOfUser(String userEmail) {
        List<PaymentMethodType> paymentMethodTypes = new ArrayList<>();
        Optional<UserPaymentCustomer> userPaymentCustomer = userPaymentCustomerDataProvider.getUserPaymentCustomerByUserEmail(userEmail);
        userPaymentCustomer.orElseThrow(() -> {
            log.error("ResourceNotFoundException in PaymentMethodQueryService.getAllPaymentMethodOfUser: No Payment method found for user email {}", userEmail);
            throw new ResourceNotFoundException("No Payment method found for user email " + userEmail);
        });
        PaymentMethodCollection paymentMethodCollection = getAllPaymentMethodsForCustomerFromStripe(userPaymentCustomer.get().getPaymentCustomerId());
        paymentMethodCollection.getData().forEach(pm -> {
            PaymentMethodType paymentMethodType = PaymentMethodType.builder()
                    .id(pm.getId())
                    .cardCountry(pm.getCard().getCountry())
                    .cardExpirationMonth(pm.getCard().getExpMonth())
                    .cardExpirationYear(pm.getCard().getExpYear())
                    .cardLast4Digits(pm.getCard().getLast4())
                    .cardType(pm.getCard().getBrand())
                    .build();
            paymentMethodTypes.add(paymentMethodType);
        });
        return paymentMethodTypes;
    }

    @Override
    public PaymentMethodType getPaymentMethodOfUserById(String paymentMethodId, String userEmail) {
        Optional<UserPaymentCustomer> userPaymentCustomer = userPaymentCustomerDataProvider.getUserPaymentCustomerByUserEmail(userEmail);
        userPaymentCustomer.orElseThrow(() -> {
            log.error("ResourceNotFoundException in PaymentMethodQueryService.getAllPaymentMethodOfUser: No Payment method found for user email {}", userEmail);
            throw new ResourceNotFoundException("No Payment method found for user email " + userEmail);
        });
        try {
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            if (!userPaymentCustomer.get().getPaymentCustomerId().equals(paymentMethod.getCustomer())) {
                log.error("StripeAPICallException in PaymentMethodQueryService.getPaymentMethodOfUserById: PaymentMethod doesn't belong to this user email {}", userEmail);
                throw new StripeAPICallException("PaymentMethod doesn't belong to this user email " + userEmail);
            }
            return PaymentMethodType.builder()
                    .id(paymentMethod.getId())
                    .cardCountry(paymentMethod.getCard().getCountry())
                    .cardExpirationMonth(paymentMethod.getCard().getExpMonth())
                    .cardExpirationYear(paymentMethod.getCard().getExpYear())
                    .cardLast4Digits(paymentMethod.getCard().getLast4())
                    .cardType(paymentMethod.getCard().getBrand())
                    .build();
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodQueryService.getPaymentMethodOfUserById: " +
                    "Error while fetching payment method for the user email {}, Exception: {}", userEmail, ex);
            throw new StripeAPICallException("Error while fetching payment method for the user email " + userEmail);
        }
    }

    private PaymentMethodCollection getAllPaymentMethodsForCustomerFromStripe(String paymentCustomerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer", paymentCustomerId);
        params.put("type", "card");
        try {
            return com.stripe.model.PaymentMethod.list(params);
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodQueryService.getAllPaymentMethodsForCustomerFromStripe: " +
                    "Error while attaching customer from Stripe for the customer Id {}, Exception: {}", paymentCustomerId, ex);
            throw new StripeAPICallException("Error while retrieving customer from Stripe for the customer Id " + paymentCustomerId);
        }
    }

}
