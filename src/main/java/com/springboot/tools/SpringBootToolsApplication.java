package com.springboot.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Slf4j
public class SpringBootToolsApplication {

	public static void main(String[] args) {
        ApplicationContext ctx=SpringApplication.run(SpringBootToolsApplication.class, args);

        log.info("spring.profiles.active:");
        for (String str : ctx.getEnvironment().getActiveProfiles()) {
            log.info(str);
        }

        log.info("Boot Server started.");


	}

}

