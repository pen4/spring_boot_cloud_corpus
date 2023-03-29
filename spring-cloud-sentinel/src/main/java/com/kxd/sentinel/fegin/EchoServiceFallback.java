package com.kxd.sentinel.fegin;

import org.springframework.web.bind.annotation.PathVariable;

class EchoServiceFallback implements EchoService {
    @Override
    public String echo(@PathVariable("str") String str) {
        return "echo fallback";
    }
}