package com.springboot.tools.rabbitmq.direct_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author kxd
 * @date 2019/3/11 15:13
 * description:
 */
public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        if (args.length < 1) {
            System.out.println("Usages ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }
        for (String serverity : args) {
            channel.queueBind(queueName, EXCHANGE_NAME, serverity);
        }
        System.out.println("[x] waiting for messages ,to exit press c");

        DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] received ...." + message);


        });


        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
