package com.alibaba.nacos.example.spring.boot;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@NacosPropertySource(dataId = "psql.properties")
public class SpringBootMySQLApplication implements ApplicationRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootMySQLApplication.class, args);
    }

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

   /* @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> methodMap = requestMappingHandlerMapping.getHandlerMethods();
        Map<String, String> urlMap = new HashMap<>();
        for (RequestMappingInfo info : methodMap.keySet()) {
            //获取请求路径
            Set<String> directPaths = info.getDirectPaths();
            // 获取全部请求方式
            //Set<RequestMethod> Methods = info.getMethodsCondition().getMethods();
            //获取全部请求名称
            String urlName = info.getName();
            for (String url : directPaths) {
                System.out.println("urlname#" + urlName + ", url#" + url);
            }
        }
            }
*/

    @Autowired
    WebApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, RequestMappingInfoHandlerMapping> map = applicationContext.getBeansOfType(
                RequestMappingInfoHandlerMapping.class);

        map.forEach((s, requestMappingInfoHandlerMapping) -> {
            System.out.println("-------------- " + s + " --------------");
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
            handlerMethods.forEach((requestMappingInfo, handlerMethod) -> {
                System.out.println(requestMappingInfo.getDirectPaths() + ", name#" + requestMappingInfo.getName() + ", method#" + requestMappingInfo.getMethodsCondition().getMethods());
            });
        });

    }
}
