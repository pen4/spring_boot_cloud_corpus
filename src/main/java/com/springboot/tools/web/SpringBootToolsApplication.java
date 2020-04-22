package com.springboot.tools.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, RabbitAutoConfiguration.class })

@Slf4j
public class SpringBootToolsApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringBootToolsApplication.class, args);
        log.info("spring.profiles.active:");
        for (String str : ctx.getEnvironment().getActiveProfiles()) {
            log.info(str);
        }
        log.info("Boot Server started.");
    }
}

