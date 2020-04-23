package com.springboot.tools.web.domain.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author kxd
 * @date 2018/11/2 15:08
 * description:
 */
@Slf4j
public class SelfBeanLoader {

    public static void autoLoadBean(ConfigurableApplicationContext applicationContext) {
        /*Reflections reflections = new Reflections("springboot.bean");

        // 获取 @SelfBean 标注的接口
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(SelfBean.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            registerBean(applicationContext, aClass.getSimpleName(), aClass);

        }
        log.info("afterPropertiesSet is {}", typesAnnotatedWith);*/
    }

    private static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz,
                                      Object... args) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if (args.length > 0) {
            for (Object arg : args) {
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }
}
