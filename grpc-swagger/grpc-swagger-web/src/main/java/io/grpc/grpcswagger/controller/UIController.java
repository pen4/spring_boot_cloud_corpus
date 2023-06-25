package io.grpc.grpcswagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UIController {

    @RequestMapping("/")
    public String index() {
        return "redirect:ui/r.html";
    }
}
