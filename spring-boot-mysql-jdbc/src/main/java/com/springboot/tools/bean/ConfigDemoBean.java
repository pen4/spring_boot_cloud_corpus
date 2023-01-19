package com.springboot.tools.bean;

import lombok.Data;

/**
 * @author kxd
 * @date 2018/11/2 14:37
 * description:
 */
@Data
public class ConfigDemoBean {
    private String type = "ConfigDemoBean";

    public String getName(String name) {
        return name + " _" + type;
    }
}
