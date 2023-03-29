package com.kxd.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @SentinelResource(value = "say")
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
