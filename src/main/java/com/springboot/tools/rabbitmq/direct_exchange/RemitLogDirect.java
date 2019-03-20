package com.springboot.tools.rabbitmq.direct_exchange;

import com.google.common.base.Preconditions;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.Charset;

/**
 * @author kxd
 * @date 2019/3/11 14:58
 * description:
 */
public class RemitLogDirect {
    public static final String EXCHANGE_NAME = "direct_log";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String serverity = getServerity(args);
            String message = getMessage(args);
            channel.basicPublish(EXCHANGE_NAME, serverity, null, message.getBytes(Charset.defaultCharset()));
            System.out.println(" [x] send '" + serverity + ":" + message + "'");
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

    private static String getServerity(String[] args) {
        Preconditions.checkNotNull(args, "传入参数%s不能为空", args);

        for (String type : args) {
            if (type.equals("error")) {
                return "error";
            }
            if (type.equals("info")) {
                return "info";
            }
            if (type.equals("warn")) {
                return "warn";
            }
        }
        return "undefined";
    }
}
