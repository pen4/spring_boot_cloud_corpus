package com.kxd.sentinel;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

/**
 * @author: User
 * @date: 2023/4/6
 * @Description:此类用于xxx
 */
public class InvokeTest {

    public static void main(String[] args) {
        try {
            test();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String test() throws ClassNotFoundException {

        String className = System.getProperty("UserService");
        Class<?> aClass = Class.forName(className);

        Object o = Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[]{aClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.getName();
            }
        });

        return o.toString();
    }
}
