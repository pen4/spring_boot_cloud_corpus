package com.springboot.tools.web.controller.nacos;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kxd
 * @date 2019/1/18 14:21
 * description:
 */
@Controller
@RequestMapping("config")
public class ConfigController {
    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }
}
