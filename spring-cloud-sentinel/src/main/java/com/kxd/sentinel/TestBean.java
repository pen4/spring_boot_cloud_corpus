package com.kxd.sentinel;

import com.kxd.sentinel.service.AService;
import com.kxd.sentinel.service.BService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: User
 * @date: 2023/3/24
 * @Description:此类用于Bean中方法是否含有static的区别
 */
@Component
public class TestBean {
//BeanDefinition 这样会生成一个bd 并把isFactoryMethodUnique改成false,之后再判断用哪个去构造
    @Bean
    public static AService aService(){
        return new AService();
    }

    @Bean
    public AService aService(BService bService){
        return new AService();
    }
}
