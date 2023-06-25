package com.my.plus.gen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: User
 * @date: 2023/6/21
 * @Description:此类用于xxx
 */
@Configuration
public class TestConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }
}
