package com.learning.bookstore.admin_client.service.impl;

import com.learning.bookstore.admin_client.exception.KafkaClientException;
import com.learning.bookstore.admin_client.service.KafkaAdminClientService;
import com.learning.bookstore.admin_client.config.RetryConfigData;
import com.learning.bookstore.config.KafkaConfigData;
import com.learning.bookstore.config.KafkaTopicConfigData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import static net.logstash.logback.argument.StructuredArguments.kv;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAdminClient implements KafkaAdminClientService {
    private final KafkaConfigData kafkaConfigData;
    private final KafkaTopicConfigData kafkaTopicConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    @PostConstruct
    public void init() {
        //Check if Schema Registry server is ready
        checkSchemaRegistry();
    }

    @Override
    public void createTopic() {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
            log.info("Create topic status result {}", kv("createTopicResult",createTopicsResult.values().values()));
        } catch (Throwable t) {
            log.error("KafkaClientException in KafkaAdminClient.createTopic()", kv("exception", t.getLocalizedMessage()));
            throw new KafkaClientException("Reached max number of retry for creating kafka topic(s)!", t);
        }
        checkTopicsCreated();
    }

    @Override
    public void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while (!isTopicCreated(topics, kafkaTopicConfigData.getTopicName())) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
            topics = getTopics();
        }
        log.info("topic is created in Kafka {}", kv("topicName", kafkaTopicConfigData.getTopicName()));
    }

    @Override
    public void checkSchemaRegistry() {
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
        log.info("Schema Registry Server is up and running!");
    }

    private HttpStatus getSchemaRegistryStatus() {
        try {
            return webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchange()
                    .map(ClientResponse::statusCode)
                    .block();
        } catch (Exception e) {
            log.error("KafkaClientException in KafkaAdminClient.getSchemaRegistryStatus(): Schema Registry Server is unavailable", kv("exception", e.getLocalizedMessage()));
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            log.error("KafkaClientException in KafkaAdminClient.sleep(): Error while sleeping for waiting new created topics", kv("exception", e.getLocalizedMessage()));
            throw new KafkaClientException("Error while sleeping for waiting new created topics!!");
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry) {
            log.error("KafkaClientException in KafkaAdminClient.checkMaxRetry(): Reached max number of retry for reading kafka topic(s)!");
            throw new KafkaClientException("Reached max number of retry for reading kafka topic(s)!");
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (topics == null) {
            log.warn("There are no topics found in Kafka!");
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        log.info("Creating topic with retry, if needed!", kv("topicName", kafkaTopicConfigData.getTopicName()), kv("retryAttempt", retryContext.getRetryCount()));
        NewTopic kafkaTopic = TopicBuilder.name(kafkaTopicConfigData.getTopicName())
                .partitions(kafkaTopicConfigData.getNumOfPartitions())
                .replicas(kafkaTopicConfigData.getReplicationFactor())
                .build();
        return adminClient.createTopics(List.of(kafkaTopic));
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            log.error("KafkaClientException in KafkaAdminClient.getTopics(): Reached max number of retry for reading kafka topic(s)!", kv("exception", t.getLocalizedMessage()));
            throw new KafkaClientException("Reached max number of retry for reading kafka topic(s)!", t);
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if (topics != null) {
            topics.forEach(topic -> log.debug("Fetched topic with name {}", kv("topicName", topic.name())));
        }
        log.info("Received list of topics from Kafka {}", kv("numberOfTopics", topics.size()));
        return topics;
    }

}
