package com.bookstore.learning.application.service;

import com.bookstore.learning.application.exception.StripeAPICallException;
import com.bookstore.learning.application.port.in.IPaymentMethodCommandService;
import com.bookstore.learning.application.port.out.IUserPaymentCustomerDataProvider;
import com.bookstore.learning.domain.Card;
import com.bookstore.learning.domain.UserPaymentCustomer;
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

    @Override
    public String createPaymentMethod(Card card, String loggedInUserEmail) {
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
        params.put("description", "Creating Customer Account for UserId : " + loggedInUserEmail);
        try {
            return Customer.create(params).getId();
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.createCustomerAtStripe: " +
                    "Error while setting up payment customer in Stripe for the user email {}, Exception: {}", loggedInUserEmail, ex);
            throw new StripeAPICallException("Error while setting up payment customer in Stripe for the user email " + loggedInUserEmail);
        }
    }

    private String createPaymentMethodAtStripe(Card cardRequest) {
        Map<String, Object> card = new HashMap<>();
        card.put("number", cardRequest.getCardNumber());
        card.put("exp_month", cardRequest.getExpirationMonth());
        card.put("exp_year", cardRequest.getExpirationYear());
        card.put("cvc", cardRequest.getCvv());
        Map<String, Object> params = new HashMap<>();
        params.put("type", "card");
        params.put("card", card);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(params);
            return paymentMethod.getId();
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.createPaymentMethodAtStripe: " +
                    "Error while setting up payment method in stripe for the Card with First name {}, Exception: {}", cardRequest.getFirstName(), ex);
            throw new StripeAPICallException("Error while setting up payment method in stripe for the Card with First name " + cardRequest.getFirstName());
        }
    }

    private void linkCustomerAndPaymentMethod(String paymentMethodId, String customerId) {
        PaymentMethod paymentMethod = null;
        try {
            paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.linkCustomerAndPaymentMethod: " +
                    "Error while retrieving payment method from Stripe for the customer Id {}, Exception: {}", customerId, ex);
            throw new StripeAPICallException("Error while retrieving payment method from Stripe for the customer Id " + customerId);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        try {
            PaymentMethod updatedPaymentMethod = paymentMethod.attach(params);
        } catch (StripeException ex) {
            log.error("StripeAPICallException in PaymentMethodCommandService.linkCustomerAndPaymentMethod: " +
                    "Error while attaching payment method from Stripe for the customer Id {}, Exception: {}", customerId, ex);
            throw new StripeAPICallException("Error while retrieving payment method from Stripe for the customer Id " + customerId);
        }
    }

}
