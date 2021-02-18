package com.bookstore.learning.infrastructure.constant;

public class PaymentServiceConstant {
    public static final String ACCESS_TOKEN_EMAIL_FIELD_NAME = "email";

    //Related to create payment customer in Stripe API call
    public static final String PAYMENT_CUSTOMER_DESCRIPTION_KEY = "description";
    public static final String PAYMENT_CUSTOMER_DESCRIPTION_VALUE = "Creating Customer Account for UserId: ";

    //Related to Payment Method Stripe API Call
    public static final String PAYMENT_AMOUNT = "amount";
    public static final String PAYMENT_CURRENCY = "currency";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String PAYMENT_CUSTOMER = "customer";
    public static final String PAYMENT_CONFIRM = "confirm";
    public static final String PAYMENT_TYPE = "type";
    public static final String PAYMENT_CARD = "card";

    //Related to Do Payment Stripe API Call
    public static final String PAYMENT_METHOD_NUMBER = "number";
    public static final String PAYMENT_METHOD_EXP_MONTH = "exp_month";
    public static final String PAYMENT_METHOD_EXP_YEAR = "exp_year";
    public static final String PAYMENT_METHOD_CVC = "cvc";

}
