package com.springboot.tools.web.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.tools.web.domain.bean.AutoBean;
import com.springboot.tools.web.domain.bean.AutoConfBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kxd
 * @date 2018/11/2 14:49
 * description:
 */
@RestController
public class DemoController {
    /**
     * 测试引入第三方包的情况
     */
    @Autowired
    private AutoBean autoBean;

    @Autowired(required = false)
    private AutoConfBean autoConfBean;

    @RequestMapping(value = "/show")
    public String show(String name) {
        Map<String, String> map = new HashMap<>(4);
        // ...
        map.put("auto", autoBean != null ? autoBean.getName() : "null");
        map.put("autoConf", autoConfBean != null ? autoConfBean.getName() : "null");
        return JSON.toJSONString(map);
    }

}
