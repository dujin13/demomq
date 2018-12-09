package com.demo.mq.rabbitmq.service;


import com.demo.mq.rabbitmq.config.RabbitMQConnection;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ
 */
@Service
public class RabbitMQProducer {
    /**
     * Exchange Name: direct模式
     */
    private static String EXCHANGE_NAME_DIRECT_CREATE = "test-direct.project.create";
    private static String EXCHANGE_NAME_DIRECT_PUBLISH = "test-direct.project.publish";
    private static String EXCHANGE_NAME_DIRECT_DELIST = "test-direct.project.delist";

    /**
     * Exchange Name: fanout模式
     */
    private static String EXCHANGE_NAME_FANOUT = "test-fanout";

    /**
     * Exchange Name: topic模式
     */
    private static String EXCHANGE_NAME_TOPIC_GZ_CREATE = "test-topic.project.gz.create";
    private static String EXCHANGE_NAME_TOPIC_GZ_PUBLISH = "test-topic.project.gz.publish";
    private static String EXCHANGE_NAME_TOPIC_GZ_DELIST = "test-topic.project.gz.delist";
    private static String EXCHANGE_NAME_TOPIC_JC_CREATE = "test-topic.project.jc.create";
    private static String EXCHANGE_NAME_TOPIC_JC_PUBLISH = "test-topic.project.jc.publish";
    private static String EXCHANGE_NAME_TOPIC_JC_DELIST = "test-topic.project.jc.delist";

    private static Logger S_LOGGER = LogManager.getLogger(RabbitMQProducer.class);

    @Autowired
    private RabbitMQConnection rabbitMQConnection;

    /**
     * 发送消息(direct方式)
     */
    public void sendMessageByDirect(String exchangeName, String msg) {
        try (Connection conn = rabbitMQConnection.createConnection()) {
            Channel detailChannel = conn.createChannel();
            detailChannel.exchangeDeclare(exchangeName, "direct");
            System.out.println("发送消息(direct): exchangeName=" + exchangeName + ", msg=" + msg);
            detailChannel.basicPublish(exchangeName, "", null, msg.getBytes());
            detailChannel.close();
        } catch (Exception e) {
            S_LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * 发送消息(fanout方式)
     */
    public void sendMessageByFanout(String exchangeName, String msg) {
        try (Connection conn = rabbitMQConnection.createConnection()) {
            Channel detailChannel = conn.createChannel();
            detailChannel.exchangeDeclare(exchangeName, "fanout");
            System.out.println("发送消息(fanout): exchangeName=" + exchangeName + ", msg=" + msg);
            detailChannel.basicPublish(exchangeName, "", null, msg.getBytes());
            detailChannel.close();
        } catch (Exception e) {
            S_LOGGER.warn(e.getMessage(), e);
        }
    }

    /**
     * 发送消息(topic方式)
     */
    public void sendMessageByTopic(String exchangeName, String routingKey, String msg) {
        try (Connection conn = rabbitMQConnection.createConnection()) {
            Channel detailChannel = conn.createChannel();
            detailChannel.exchangeDeclare(exchangeName, "topic");
            System.out.println("发送消息(topic): exchangeName=" + exchangeName + ", msg=" + msg);
            detailChannel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
            detailChannel.close();
        } catch (Exception e) {
            S_LOGGER.warn(e.getMessage(), e);
        }
    }
}