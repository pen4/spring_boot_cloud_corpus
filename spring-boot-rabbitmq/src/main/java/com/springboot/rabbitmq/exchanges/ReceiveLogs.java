package com.springboot.rabbitmq.exchanges;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * @author kxd
 * @date 2019/3/8 10:42
 * description:
 */
public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //随机给定队列名
        String queueName = channel.queueDeclare().getQueue();
        //建立binding
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("[x] waiting for messages .to exit press c");

      /*  DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] received '" + message + "'");
        });
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });*/
    }
}
