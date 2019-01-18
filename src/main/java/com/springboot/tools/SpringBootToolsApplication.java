package com.springboot.tools;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Slf4j
@NacosPropertySource(dataId ="example", autoRefreshed = true)

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

