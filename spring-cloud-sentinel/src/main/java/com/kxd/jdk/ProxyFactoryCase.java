package com.kxd.jdk;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author: User
 * @date: 2023/3/30
 * @Description:此类用于xxx
 */
public class ProxyFactoryCase {

    public static void main(String[] args) {
        UserService target = new UserService();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before...");
                Object result = invocation.proceed();
                System.out.println("after...");
                return result;
            }
        });

        UserInterface userService = (UserInterface) proxyFactory.getProxy();
        userService.test();
    }
}
