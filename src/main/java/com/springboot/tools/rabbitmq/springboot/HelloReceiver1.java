package com.springboot.tools.rabbitmq.springboot;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author kxd
 * @date 2019/3/12 14:42
 * description:
 */
@Component
@RabbitListener(queues = "helloQueue")
public class HelloReceiver1 {

    @RabbitHandler
    public void process(String hello){
        System.out.println("Receiver 1  "+hello);
    }
}
