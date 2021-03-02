package com.learning.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.learning.bookstore.client")
public class BookstoreCartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreCartServiceApplication.class, args);
	}

}
