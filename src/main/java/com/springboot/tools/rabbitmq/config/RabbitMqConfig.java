package com.springboot.tools.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author kxd
 * @date 2019/3/7 17:03
 * description:
 */

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue DistribuQueue(){
        return new Queue("distribu");
    }
}
