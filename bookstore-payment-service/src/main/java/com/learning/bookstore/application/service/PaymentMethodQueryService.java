package com.learning.bookstore.application.service;

import com.learning.bookstore.application.exception.ResourceNotFoundException;
import com.learning.bookstore.application.exception.StripeAPICallException;
import com.learning.bookstore.application.port.in.IPaymentMethodQueryService;
import com.learning.bookstore.application.port.out.IUserPaymentCustomerDataProvider;
import com.learning.bookstore.domain.PaymentMethodType;
import com.learning.bookstore.domain.UserPaymentCustomer;
import com.learning.bookstore.infrastructure.constant.PaymentServiceConstant;
import com.learning.bookstore.infrastructure.util.PrincipalResolver;
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
    private final PrincipalResolver principalResolver;

    @Override
    public List<PaymentMethodType> getAllPaymentMethodOfUser() {
        String userEmail = principalResolver.getCurrentLoggedInUserMail();
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
    public PaymentMethodType getPaymentMethodOfUserById(String paymentMethodId) {
        String userEmail = principalResolver.getCurrentLoggedInUserMail();
        Optional<UserPaymentCustomer> userPaymentCustomer = userPaymentCustomerDataProvider.getUserPaymentCustomerByUserEmail(userEmail);
        exitIfNoPaymentCustomerFound(userPaymentCustomer);
        try {
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            exitIfUnauthorized(userPaymentCustomer, paymentMethod);
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
            throw new StripeAPICallException("Error while fetching payment method for the user email "
                    + userEmail + ". Exception: " + ex);
        }
    }

    private PaymentMethodCollection getAllPaymentMethodsForCustomerFromStripe(String paymentCustomerId) {
        Map<String, Object> params = new HashMap<>();
        params.put(PaymentServiceConstant.PAYMENT_CUSTOMER, paymentCustomerId);
        params.put(PaymentServiceConstant.PAYMENT_TYPE, PaymentServiceConstant.PAYMENT_CARD);
        try {
            return PaymentMethod.list(params);
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodQueryService.getAllPaymentMethodsForCustomerFromStripe: " +
                    "Error while attaching customer from Stripe for the customer Id {}, Exception: {}", paymentCustomerId, ex);
            throw new StripeAPICallException("Error while retrieving customer from Stripe for the customer Id "
                    + paymentCustomerId + ". Exception: " + ex);
        }
    }

    private void exitIfNoPaymentCustomerFound(Optional<UserPaymentCustomer> userPaymentCustomer) {
        userPaymentCustomer.orElseThrow(() -> {
            log.error("ResourceNotFoundException in PaymentMethodQueryService.getAllPaymentMethodOfUser: No Payment method found for user email {}",
                    principalResolver.getCurrentLoggedInUserMail());
            throw new ResourceNotFoundException("No Payment method found for user email " + principalResolver.getCurrentLoggedInUserMail());
        });
    }

    private void exitIfUnauthorized(Optional<UserPaymentCustomer> userPaymentCustomer, PaymentMethod paymentMethod) {
        if (!userPaymentCustomer.get().getPaymentCustomerId().equals(paymentMethod.getCustomer())) {
            log.error("StripeAPICallException in PaymentMethodQueryService.getPaymentMethodOfUserById: PaymentMethod with ID {} doesn't belong to this user email {}",
                    paymentMethod.getId(), principalResolver.getCurrentLoggedInUserMail());
            throw new StripeAPICallException("PaymentMethod with ID " + paymentMethod.getId() + " doesn't belong to this user email "
                    + principalResolver.getCurrentLoggedInUserMail());
        }
    }

}
