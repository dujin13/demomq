package com.demo.mq.activemq.rest;

import com.demo.mq.activemq.ActiveMqProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;

/**
 * Created by gaolp on 2018/11/28.
 */
@RestController
@RequestMapping("/activemq")
public class ActiveMqClient {

    @Autowired
    private ActiveMqProducer producer;

    @RequestMapping(value="/queue", method = RequestMethod.GET)
    public void queue(){
        Destination queueDestination = new ActiveMQQueue("test.queue");

        for(int i=0; i<100; i++){
            producer.sendMessage(queueDestination, "queue data " + i);
        }
    }

    @RequestMapping(value="/topic", method = RequestMethod.GET)
    public void topic(){
        Destination topicDestination = new ActiveMQTopic("test.topic");

        for(int i=0; i<100; i++){
            producer.sendMessage(topicDestination, "topic data " + i);
        }
    }
}
