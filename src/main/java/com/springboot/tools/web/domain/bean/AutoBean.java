package com.springboot.tools.web.domain.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kxd
 * @date 2018/11/2 14:54
 * description:
 */
@Slf4j
@Data
public class AutoBean {
    private String name;

    public AutoBean() {
        this("defaultAutoBean");
    }

    public AutoBean(String name) {
        this.name = name;
        log.info("AutoBean load time: {}", System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }
}
