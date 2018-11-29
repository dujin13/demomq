package com.demo.mq;

import com.demo.mq.activemq.ActiveMqConsumer;
import com.demo.mq.activemq.ActiveMqProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;
import java.util.concurrent.TimeUnit;

/**
 * Created by gaolp on 2018/11/28.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJms
public class ActiveMqClientTest {

    @Autowired
    private ActiveMqProducer producer;

    @Test
    public void sendQueue() throws InterruptedException {
        Destination queueDestination = new ActiveMQQueue("test.queue");

        for(int i=0; i<100; i++){
            producer.sendMessage(queueDestination, "queue data " + i);
        }

        // 等待所有队列都输出完数据
        TimeUnit.SECONDS.sleep(300);
    }

    @Test
    public void sendTopic() throws InterruptedException {
        Destination topicDestination = new ActiveMQTopic("test.topic");

        for(int i=0; i<100; i++){
            producer.sendMessage(topicDestination, "topic data " + i);
        }

        // 等待所有队列都输出完数据
        TimeUnit.SECONDS.sleep(300);
    }
}
