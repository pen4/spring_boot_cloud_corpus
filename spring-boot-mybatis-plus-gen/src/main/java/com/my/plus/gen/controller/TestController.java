package com.my.plus.gen.controller;

import com.my.plus.gen.entity.R;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: User
 * @date: 2023/6/21
 * @Description:此类用于xxx
 */
@Controller
public class TestController {


    @RequestMapping(method = RequestMethod.GET, path = "/test", produces = "text/plain")
    @ResponseBody
    public R test() {
        return R.ok();
    }
}
