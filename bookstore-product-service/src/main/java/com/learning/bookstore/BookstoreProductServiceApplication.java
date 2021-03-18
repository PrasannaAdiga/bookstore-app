package com.learning.bookstore;

import com.learning.bookstore.admin_client.service.impl.KafkaAdminClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BookstoreProductServiceApplication implements CommandLineRunner {
    private final KafkaAdminClient kafkaAdminClient;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreProductServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		kafkaAdminClient.createTopic();
	}

}
