package com.kxd.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: User
 * @date: 2023/3/30
 * @Description:此类用于xxx
 */
public class JdkProxyCase {
    public static void main(String[] args) {
        UserService target = new UserService();

        // UserInterface接口的代理对象
        Object proxy = Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{UserInterface.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("before...");
                Object result = method.invoke(target, args);
                System.out.println("after...");
                return result;
            }
        });

        UserInterface userService = (UserInterface) proxy;
        userService.test();
    }
}
