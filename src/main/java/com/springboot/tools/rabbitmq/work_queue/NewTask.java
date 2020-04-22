package com.springboot.tools.rabbitmq.work_queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @author kxd
 * @date 2019/3/7 19:50
 * description:
 */
public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); ) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            String[] name={"a","b","c"};
            String message = String.join(" ", name);
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println(" [x] send '" + message + "'");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
