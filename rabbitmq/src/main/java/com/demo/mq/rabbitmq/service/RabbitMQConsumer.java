package com.demo.mq.rabbitmq.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ消费者
 */

@Component
public class RabbitMQConsumer extends RabbitMQSubscribe {

    @Override
    protected Table<String, String, String> getExchangeQueue() {
        Table<String, String, String> data = HashBasedTable.create();

        // direct方式
        data.put("direct-upd", "queueName", "test-queue-direct-upd");
        data.put("direct-upd", "exchangeNames", "test-direct.project.create");
        data.put("direct-upd", "type", "direct");

        data.put("direct-state", "queueName", "test-queue-direct-state");
        data.put("direct-state", "exchangeNames", "test-direct.project.publish,test-direct.project.delist");
        data.put("direct-state", "type", "direct");

        // fanout方式
        data.put("fanout-1", "queueName", "test-queue-fanout-1");
        data.put("fanout-1", "exchangeNames", "test-fanout");
        data.put("fanout-1", "type", "fanout");

        data.put("fanout-2", "queueName", "test-queue-fanout-1");
        data.put("fanout-2", "exchangeNames", "test-fanout");
        data.put("fanout-2", "type", "fanout");

        data.put("fanout-3", "queueName", "test-queue-fanout-2");
        data.put("fanout-3", "exchangeNames", "test-fanout");
        data.put("fanout-3", "type", "fanout");

        // topic方式
        data.put("topic-project", "queueName", "test-queue-topic-project");
        data.put("topic-project", "exchangeNames", "test-topic.project");
        data.put("topic-project", "routingKey", "project.#");
        data.put("topic-project", "type", "topic");

        data.put("topic-publish", "queueName", "test-queue-topic-publish");
        data.put("topic-publish", "exchangeNames", "test-topic.project");
        data.put("topic-publish", "routingKey", "project.*.publish");
        data.put("topic-publish", "type", "topic");

        data.put("topic-gz", "queueName", "test-queue-topic-publish-gz");
        data.put("topic-gz", "exchangeNames", "test-topic.project");
        data.put("topic-gz", "routingKey", "project.gz.*");
        data.put("topic-gz", "type", "topic");

        return data;
    }
}
