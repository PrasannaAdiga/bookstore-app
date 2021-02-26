package com.learning.bookstore;

import com.stripe.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstorePaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstorePaymentServiceApplication.class, args);
		Stripe.apiKey = "sk_test_51IMBd2DGcSFaTwuTPpNXnLsBIxewmJODAWnq7vIlDgVzUoyWl17smUHZUFh0scyLKMdCDQmKTdOhwr4FdEfqbEXD008UTZXfCo";
	}

}
