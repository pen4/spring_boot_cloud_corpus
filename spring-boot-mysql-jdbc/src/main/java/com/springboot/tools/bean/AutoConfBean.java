package com.springboot.tools.bean;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kxd
 * @date 2018/11/2 14:57
 * description:
 */
@Slf4j
public class AutoConfBean {
    private String name;

    public AutoConfBean(String name) {
        this.name = name;
        log.info("AutoConfBean load time: {}", System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }
}
