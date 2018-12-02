package com.demo.mq.rocketmq.rest;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gaolp on 2018/11/28.
 */
@RestController
@RequestMapping("/rocketmq")
public class RecketMqClient {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @RequestMapping(value="/topic", method = RequestMethod.GET)
    public void topic() throws Exception {

        for(int i=0; i<100; i++){
            String msg = "demo msg test";
            System.out.println("开始发送消息："+msg);
            Message sendMsg = new Message("DemoTopic","DemoTag",msg.getBytes());
            //默认3秒超时
            SendResult sendResult = defaultMQProducer.send(sendMsg);
            System.out.println("消息发送响应信息："+sendResult.toString());
        }
    }

}
