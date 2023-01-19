package com.springboot.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author kxd
 * @date 2019/3/7 17:05
 * description:
 */
@Component
public class DistributionReceiver {

    @RabbitListener(queues = "distribu")
    public void processA(Message message) {
        String msg = new String(message.getBody());
        System.out.println("distribuReceiverA:" + msg);
        LocalTime localTime = LocalTime.now();
        System.out.println("ProcessingA at :" + localTime.toString());

        try {
            for (char ch : msg.toCharArray()) {
                if (ch == '.') {
                    doWork(100);
                }
            }
        } catch (InterruptedException e) {

        } finally {
            System.out.println("A Done ! at" + LocalTime.now());
        }
    }

    public void doWork(long time) throws InterruptedException {
        Thread.sleep(time);
    }
}
