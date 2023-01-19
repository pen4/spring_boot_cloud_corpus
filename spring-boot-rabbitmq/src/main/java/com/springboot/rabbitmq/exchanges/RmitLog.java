package com.springboot.rabbitmq.exchanges;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author kxd
 * @date 2019/3/8 10:36
 * description:
 */
public class RmitLog {
    private static final String EXCHANGE_NAME = "logs";
/*
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            //定义广播类型exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = "this is from a exchange message";

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] send message。。。");
        } catch (Exception e) {

        }

    }*/
}
