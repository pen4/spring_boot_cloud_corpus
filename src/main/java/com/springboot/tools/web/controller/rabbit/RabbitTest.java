package com.springboot.tools.web.controller.rabbit;

import com.springboot.tools.web.service.rabbit.HelloSender1;
import com.springboot.tools.web.service.rabbit.HelloSender2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kxd
 * @date 2019/3/12 14:46
 * description:
 */

@RestController
@RequestMapping("/rabbit")
public class RabbitTest {

    @Autowired
    private HelloSender1 helloSender1;
    @Autowired
    private HelloSender2 helloSender2;

    @PostMapping("/hello")
    public void hello() {
        helloSender1.send();

    }

    @PostMapping("/oneTomany")
    public void hello2() {
        for (int i = 0; i < 10; i++) {
            helloSender1.send();
        }
    }

    @PostMapping("/manyTomany")
    public void hello3() {
        for (int i = 0; i < 10; i++) {
            helloSender1.send();
            helloSender2.send();
        }
    }
}
