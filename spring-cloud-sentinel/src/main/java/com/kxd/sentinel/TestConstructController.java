package com.kxd.sentinel;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: User
 * @date: 2023/3/20
 * @Description:此类用于 方法注入
 */
@RestController
public class TestConstructController {
    public TestConstructService testConstructService;

    @PostMapping
    @Lookup(value = "testConstructService")
    public void sayHello() {
        testConstructService.sayHello();
    }


}
