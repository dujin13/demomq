package com.demo.mq.rabbitmq.rest;

import com.demo.mq.rabbitmq.service.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gaolp on 2018/12/09.
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitMqClient {

    @Autowired
    private RabbitMQProducer producer;

    private static final int SIZE = 10;

    @RequestMapping(value="/direct", method = RequestMethod.GET)
    public void direct(){
        for(int i=0; i< SIZE; i++) {
            producer.sendMessageByDirect("test-direct.project.create", "测试消息:direct-create:" + i);
            producer.sendMessageByDirect("test-direct.project.publish", "测试消息:direct-publish:" + i);
            producer.sendMessageByDirect("test-direct.project.delist", "测试消息:direct-delist:" + i);
        }
    }

    @RequestMapping(value="/fanout", method = RequestMethod.GET)
    public void fanout(){
        for(int i=0; i< SIZE; i++) {
            producer.sendMessageByFanout("test-fanout", "测试消息:fanout:" + i);
        }
    }

    @RequestMapping(value="/topic", method = RequestMethod.GET)
    public void topic(){
        for(int i=0; i< SIZE; i++) {
            producer.sendMessageByTopic("test-topic.project", "project.gz.create", "测试消息:topic-gz-create:" + i);
            producer.sendMessageByTopic("test-topic.project", "project.gz.publish", "测试消息:topic-gz-publish:" + i);
            producer.sendMessageByTopic("test-topic.project", "project.gz.delist", "测试消息:topic-gz-delist:" + i);

            producer.sendMessageByTopic("test-topic.project", "project.jc.create", "测试消息:topic-jc-create:" + i);
            producer.sendMessageByTopic("test-topic.project", "project.jc.publish", "测试消息:topic-jc-publish:" + i);
            producer.sendMessageByTopic("test-topic.project", "project.jc.delist", "测试消息:topic-jc-delist:" + i);
        }
    }
}
