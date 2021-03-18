package com.learning.bookstore.producer.service.impl;

import com.learning.bookstore.model.product.Product;
import com.learning.bookstore.producer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductKafkaProducer implements KafkaProducerService<Long, Product> {
    private final KafkaTemplate<Long, Product> kafkaTemplate;

    @Override
    public void send(String topicName, Long key, Product message) {
        log.info("Sending message='{}' to topic='{}'", message, topicName);
        ListenableFuture<SendResult<Long, Product>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
        addCallback(topicName, message, kafkaResultFuture);
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }

    private void addCallback(String topicName, Product message, ListenableFuture<SendResult<Long, Product>> kafkaResultFuture) {
        kafkaResultFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
            }

            @Override
            public void onSuccess(SendResult<Long, Product> result) {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.debug("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            metadata.timestamp(),
                            System.nanoTime());
            }
        });
    }

}
