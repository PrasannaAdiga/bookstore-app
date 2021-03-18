package com.learning.bookstore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-topic-config")
public class KafkaTopicConfigData {
    private String topicName;
    private int numOfPartitions;
    private short replicationFactor;
}
