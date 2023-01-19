package com.springboot.tools.config;

import com.springboot.tools.bean.AnoDemoBean;
import com.springboot.tools.bean.ConfigDemoBean;
import com.springboot.tools.bean.DemoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kxd
 * @date 2018/11/2 14:38
 * description:
 */

@Configuration
public class BeanLoadConfig {

    @Bean
    public ConfigDemoBean configDemoBean() {
        return new ConfigDemoBean();
    }

    @Bean(value = "AnoDemoBean2")
    public AnoDemoBean anotherConfigBean() {
        return new AnoDemoBean();
    }

    @Bean
    public DemoFactoryBean demoFactoryBean() {
        return new DemoFactoryBean();
    }


}
