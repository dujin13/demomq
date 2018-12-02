package com.demo.mq.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by gaolp on 2018/11/28.
 */

@Component
public class ActiveMqConsumer {
    @JmsListener(destination = "test.queue",containerFactory="jmsListenerContainerQueue")
    public void receiveQueue(String text) {
        System.out.println("Consumer收到的Queue报文为:"+text);
    }

    @JmsListener(destination = "test.topic",containerFactory="jmsListenerContainerTopic")
    public void receiveTopic1(String text) {
        System.out.println("Consumer1收到的topic报文为:"+text);
    }

    @JmsListener(destination = "test.topic",containerFactory="jmsListenerContainerTopic")
    public void receiveTopic2(String text) {
        System.out.println("Consumer2收到的topic报文为:"+text);
    }
}
