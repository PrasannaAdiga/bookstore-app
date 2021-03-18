package com.learning.bookstore.admin_client.service;

public interface KafkaAdminClientService {
    public void createTopic();
    public void checkTopicsCreated();
    public void checkSchemaRegistry();
}
