# Supported Functionalities
Below steps are followed to perform any payment through Stripe

###1. Payment Customer.
 - Create a Payment Customer in stripe for the logged in user email
 - Stripe will return Payment Customer ID which is created at Stripe end, which will be saved in payment service DB along with logged in user mail
 - createPaymentMethod REST API will be used for this purpose

###2. Payment Method.
 - Once payment customer created in Stripe and saved in payment service DB, create a Payment Method in Stripe which will contains specific Card details
 - User can create multiple Payment Method in Stripe for each type of Card
 - createPaymentMethod REST API will be used for this purpose
 - Also, User can fetch all the created payment methods from Stripe, or they can fetch a specific Payment Method from Stripe by using REST API
 - For creating a payment method below payload can be used
    ```
    {
        "firstName": "firstName",
        "lastName": "lastName",
        "cardNumber": "5555555555554444",
        "last4Digits": "4444",
        "expirationMonth": "12",
        "expirationYear": "2022",
        "cvv": "876"
    }
    ```
 - Sample card number can be obtained from the link: https://stripe.com/docs/testing  

###3. Link Payment Customer and Payment Methods.
 - Once payment customer and payment method created in Stripe, user need to link these two together
 - createPaymentMethod REST API will do this linking automatically

###4. Do actual payment.
 - Once Payment customer and payment method created in Stripe, user can do the actual payment by providing the actual product amount and currency along with specific payment method ID which is already created in Stripe
 - For doing payment we can use the provided REST API with below payload. Replace the payment method ID by actual payment method ID which is created in Stripe
    ```
     {
         "amount": "50",
         "currency": "inr",
         "paymentMethodId": "pm_1IMCI1DGcSFaTwuT74XjbZdC"
     }
     ```
   