package com.springboot.rabbitmq.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kxd
 * @date 2019/3/12 14:40
 * description:
 */
@Component
public class HelloSender2 {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(){
        String sendMsg="hello2"+new Date();
        System.out.println("Send2 s:  "+sendMsg);
        this.rabbitTemplate.convertAndSend("helloQueue",sendMsg);
    }

}
