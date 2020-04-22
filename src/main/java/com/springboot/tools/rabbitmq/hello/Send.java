package com.springboot.tools.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author kxd
 * @date 2019/3/7 19:20
 * description:
 */
public class Send {
    private final static String QUEUE_ANME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); ) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_ANME, false, false, false, null);
            String message = "hello world!";
            channel.basicPublish("", QUEUE_ANME, null, message.getBytes());
            System.out.println(" [x] Send '" + message + "'");
        } catch (Exception e) {

        }
    }
}
