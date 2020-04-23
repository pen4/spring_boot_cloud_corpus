package com.springboot.tools.web.domain.bean;

/**
 * @author kxd
 * @date 2018/11/2 14:36
 * description:
 */

public class AnoDemoBean {
    private String type = "AnoDemoBean";

    public String getName(String name) {
        return name + " _" + type;
    }

    public String sayHello(String content) {
        return content;
    }
}
