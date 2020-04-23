package com.springboot.tools.rabbitmq.topic_exchange;

import com.google.common.base.Preconditions;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author kxd
 * @date 2019/3/11 15:56
 * description:
 */
public class EmitLogTopic {
    private static final String EXCHAGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            ) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHAGE_NAME,"topic");
            String routingKey=getRouting(args);
            String message=getMessage(args);
            channel.basicPublish(EXCHAGE_NAME,routingKey,null,message.getBytes("UTF-8"));
            System.out.println("[x] send '"+message+"'");
        }
    }


    private static String getMessage(String[] args) {
        Preconditions.checkNotNull(args, "传入参数%s不能为空", args);
        String backMessage = "";
        for (String type : args) {
            //some info
        }
        return backMessage;
    }

    private static String getRouting(String[] args) {
        Preconditions.checkNotNull(args, "传入参数%s不能为空", args);

        for (String type : args) {
            if (type.equals("error")) {
                return "error.cron.a";
            }
            if (type.equals("info")) {
                return "info.kernal.a";
            }
            if (type.equals("warn")) {
                return "warn.back.b";
            }
        }
        return "undefined";
    }
}
