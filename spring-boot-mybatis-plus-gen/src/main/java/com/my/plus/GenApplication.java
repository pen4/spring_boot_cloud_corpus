package com.my.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 代码生成
 *
 * @author geovis
 */
@EnableTransactionManagement(proxyTargetClass = true)

@SpringBootApplication
public class GenApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GenApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("代码生成模块启动成功");
    }
}
