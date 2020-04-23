package com.springboot.tools.web.config;

import com.springboot.tools.web.domain.bean.AutoBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author kxd
 * @date 2018/11/2 14:50
 * description:
 */

@Configuration
@ComponentScan()
public class SelfAutoConfig {

    @Bean
    public AutoBean autoConfBean() {
        return new AutoBean("auto load + " + System.currentTimeMillis());
    }

}
