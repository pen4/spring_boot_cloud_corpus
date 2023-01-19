package com.springboot.tools.service;

import com.springboot.tools.model.Demo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author kxd
 * @date 2019/3/19 20:06
 * description:
 */
@FeignClient("service-a")
public interface UserFeignClient {
    @RequestMapping(value = "/demo/getDemoById/{id}", method = RequestMethod.GET)
    Demo getDemoById(@PathVariable("id") long id);

    @RequestMapping(value = "/demo/getDemo", method = RequestMethod.POST)
    Demo getDemo(@RequestBody Demo demo);

    @RequestMapping(value = "/add/{a}/{b}", method = RequestMethod.GET)
    String add(@PathVariable("a") Integer a, @PathVariable("b") Integer b);

    @RequestMapping(value = "/subtraction", method = RequestMethod.GET)
    String subtraction(@RequestParam("a") Integer a, @RequestParam("b") Integer b);

}
