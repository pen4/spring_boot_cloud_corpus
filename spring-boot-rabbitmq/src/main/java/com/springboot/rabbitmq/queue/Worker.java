package com.springboot.rabbitmq.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author kxd
 * @date 2019/3/7 19:57
 * description:
 */
public class Worker {


    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.basicQos(1);
        /*DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("received >>" + message);
            try {
                doWork(message);
            } finally {
                System.out.println("[x] done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        });
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
*/
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if ('.' == ch) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
