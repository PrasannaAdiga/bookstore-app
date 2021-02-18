package com.bookstore.learning.application.service;

import com.bookstore.learning.application.exception.StripeAPICallException;
import com.bookstore.learning.application.port.in.IPaymentMethodCommandService;
import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.domain.Card;
import com.bookstore.learning.domain.UserPaymentCustomer;
import com.bookstore.learning.infrastructure.constant.PaymentServiceConstant;
import com.bookstore.learning.infrastructure.util.PrincipalResolver;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PaymentMethodCommandService implements IPaymentMethodCommandService {
    private final IUserPaymentCustomerDataProvider userPaymentCustomerDataProvider;
    private final PrincipalResolver principalResolver;

    @Override
    public String createPaymentMethod(Card card) {
        String loggedInUserEmail = principalResolver.getCurrentLoggedInUserMail();
        Optional<UserPaymentCustomer> oUserPaymentCustomer = userPaymentCustomerDataProvider.getUserPaymentCustomerByUserEmail(loggedInUserEmail);
        String paymentCustomerId;
        if (!oUserPaymentCustomer.isPresent()) {
            //1. Create Payment Customer at stripe end
            paymentCustomerId = createCustomerAtStripe(loggedInUserEmail);
            UserPaymentCustomer userPaymentCustomer = UserPaymentCustomer.builder()
                    .userEmail(loggedInUserEmail)
                    .paymentCustomerId(paymentCustomerId)
                    .build();
            //2. Save it in payment service DB
            userPaymentCustomerDataProvider.createUserPaymentCustomer(userPaymentCustomer);
        } else {
            paymentCustomerId = oUserPaymentCustomer.get().getPaymentCustomerId();
        }
        //3. Create Payment Method at stripe end
        String paymentMethodId = createPaymentMethodAtStripe(card);
        //4. link Payment Customer and Payment Method at stripe end
        linkCustomerAndPaymentMethod(paymentMethodId, paymentCustomerId);
        return paymentMethodId;
    }

    private String createCustomerAtStripe(String loggedInUserEmail) {
        Map<String, Object> params = new HashMap<>();
        params.put(PaymentServiceConstant.PAYMENT_CUSTOMER_DESCRIPTION_KEY,
                PaymentServiceConstant.PAYMENT_CUSTOMER_DESCRIPTION_VALUE + loggedInUserEmail);
        try {
            return Customer.create(params).getId();
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.createCustomerAtStripe: " +
                    "Error while setting up payment customer in Stripe for the user email {}, Exception: {}", loggedInUserEmail, ex);
            throw new StripeAPICallException("Error while setting up payment customer in Stripe for the user email "
                    + loggedInUserEmail + ". Exception: " + ex);
        }
    }

    private String createPaymentMethodAtStripe(Card cardRequest) {
        Map<String, Object> card = new HashMap<>();
        card.put(PaymentServiceConstant.PAYMENT_METHOD_NUMBER, cardRequest.getCardNumber());
        card.put(PaymentServiceConstant.PAYMENT_METHOD_EXP_MONTH, cardRequest.getExpirationMonth());
        card.put(PaymentServiceConstant.PAYMENT_METHOD_EXP_YEAR, cardRequest.getExpirationYear());
        card.put(PaymentServiceConstant.PAYMENT_METHOD_CVC, cardRequest.getCvc());
        Map<String, Object> params = new HashMap<>();
        params.put(PaymentServiceConstant.PAYMENT_TYPE, PaymentServiceConstant.PAYMENT_CARD);
        params.put(PaymentServiceConstant.PAYMENT_CARD, card);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            return paymentMethod.getId();
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.createPaymentMethodAtStripe: " +
                    "Error while setting up payment method in stripe for the Card with First name {}, Exception: {}", cardRequest.getFirstName(), ex);
            throw new StripeAPICallException("Error while setting up payment method in stripe for the Card with First name "
                    + cardRequest.getFirstName() + ". Exception: " + ex);
        }
    }

    private void linkCustomerAndPaymentMethod(String paymentMethodId, String customerId) {
        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.linkCustomerAndPaymentMethod: " +
                    "Error while retrieving payment method from Stripe for the customer Id {}, Exception: {}", customerId, ex);
            throw new StripeAPICallException("Error while retrieving payment method from Stripe for the customer Id "
                    + customerId + ". Exception: " + ex);
        }
        Map<String, Object> params = new HashMap<>();
        params.put(PaymentServiceConstant.PAYMENT_CUSTOMER, customerId);
        try {
            PaymentMethod updatedPaymentMethod = paymentMethod.attach(params);
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.linkCustomerAndPaymentMethod: " +
                    "Error while attaching payment method from Stripe for the customer Id {}, Exception: {}", customerId, ex);
            throw new StripeAPICallException("Error while retrieving payment method from Stripe for the customer Id "
                    + customerId + ". Exception: " + ex);
        }
    }

}
