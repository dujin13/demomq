package com.demo.mq.rabbitmq.config;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQConnection {
    private String host;

    private Integer port;

    private String user;

    private String password;

    private ConnectionFactory cf;

    private ConnectionFactory initConnectionFactory() {
        ConnectionFactory cf = new ConnectionFactory();
        cf.setUsername(user);
        cf.setPassword(password);
        return cf;
    }

    public Connection createConnection() throws IOException, TimeoutException {
        if (cf == null) {
            cf = initConnectionFactory();
        }
        // 设置host的默认值
        if (host == null || host.trim().length() == 0) {
            host = "localhost";
        }
        String[] hostArr = host.split(",");
        Address[] addresses = new Address[hostArr.length];
        for (int i = 0; i < hostArr.length; i++) {
            addresses[i] = new Address(hostArr[i], port);
        }
        return cf.newConnection(addresses);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
