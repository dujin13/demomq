package com.demo.mq.rabbitmq.service;


import com.demo.mq.rabbitmq.config.RabbitMQConnection;
import com.google.common.base.Splitter;
import com.google.common.collect.Table;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 订阅RabbitMQ
 */
public abstract class RabbitMQSubscribe {
    private static Logger S_LOGGER = LogManager.getLogger(RabbitMQSubscribe.class);
    private static Connection conn;

    @Autowired
    private RabbitMQConnection rabbitMqConn;

    /**
     * 初始化数据
     */
    @PostConstruct
    public void contextInitialized() {
        subInfo(getExchangeQueue());
    }

    /**
     * 为每个订阅主题执行监听进行报文处理
     *
     * @param exchangeQueueMap
     */
    private void subInfo(Table<String, String, String> exchangeQueueMap) {
        S_LOGGER.info("订阅RabbitMQ开始...");
        try {
            conn = rabbitMqConn.createConnection();
            for (final Map.Entry<String, Map<String, String>> exchangeQueue : exchangeQueueMap.rowMap().entrySet()) {
                final String instanceName = exchangeQueue.getKey();
                final String queueName = exchangeQueue.getValue().get("queueName");
                final String exchangeNames = exchangeQueue.getValue().get("exchangeNames");
                final String routingKey = exchangeQueue.getValue().get("routingKey");
                final String type = exchangeQueue.getValue().get("type");

                List<String> exchangeNameList = Splitter.on(",").trimResults().splitToList(exchangeNames);

                final Channel channel;
                try {
                    channel = conn.createChannel();
                    for(String exchangeName : exchangeNameList) {
                        channel.exchangeDeclare(exchangeName, type);
                    }
                    channel.queueDeclare(queueName, true, false, false, null);// 声明消息队列，且为可持久化的
                    for(String exchangeName : exchangeNameList) {
                        channel.queueBind(queueName, exchangeName, routingKey == null ? "" : routingKey);
                    }
                    channel.basicQos(1); // 消息分发处理
                    /**
                     * 采用订阅的方式获取消息
                     */
                    S_LOGGER.info("实例[" + instanceName + "]开始订阅队列[" + queueName + "]...");
                    channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                        @Override
                        public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                            S_LOGGER.info("===" + consumerTag + "=====" + sig.getMessage());
                            boolean isOpenConnect = conn != null && conn.isOpen();
                            boolean isOpenChannel = channel != null && channel.isOpen();
                            while (!isOpenChannel || !isOpenConnect) {
                                try {
                                    S_LOGGER.warn("实例[" + instanceName + "]订阅RabbitMQ失败，重连接....");
                                    conn = rabbitMqConn.createConnection();
                                    Thread.sleep(3000);
                                    isOpenConnect = conn != null && conn.isOpen();
                                    isOpenChannel = channel != null && channel.isOpen();
                                } catch (Exception e) {
                                    S_LOGGER.warn("实例[" + instanceName + "]订阅队列[" + queueName + "]连接时发生错误," + e.getMessage(), e.getCause());
                                }
                            }
                        }

                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
                                throws IOException {
                            // 消息序号
                            long deliveryTag = envelope.getDeliveryTag();
                            String mes = new String(body, "UTF-8");
                            System.out.println("实例[" + instanceName + "]的队列[" + queueName + "]接受到消息:" + mes);
                            // 确认收到，消息回执
                            channel.basicAck(deliveryTag, false);
                        }
                    });
                    S_LOGGER.info("实例[" + instanceName + "]订阅队列[" + queueName + "]...");
                } catch (IOException e1) {
                    S_LOGGER.error("实例[" + instanceName + "]订阅队列[" + queueName + "]时出错," + e1.getMessage(), e1);
                }
            }
        } catch (IOException | TimeoutException e2) {
            S_LOGGER.error("订阅RabbitMQ时出现错误:" + e2.getMessage(), e2);
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (IOException e) {
                S_LOGGER.error("关闭RabbitMQ连接时出现错误:" + e.getMessage(), e);
            }
        }
        S_LOGGER.info("订阅RabbitMQ完成...");
    }

    /**
     * 队列名, 实例名
     */
    protected abstract Table<String, String, String> getExchangeQueue();
}