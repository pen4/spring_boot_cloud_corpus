package com.springboot.tools.bean;

/**
 * @author kxd
 * @date 2018/11/2 14:39
 * description:
 */
public class FacDemoBean {

    private String type = "FacDemoBean";

    public String getName(String name) {
        return name + " _" + type;
    }

}
