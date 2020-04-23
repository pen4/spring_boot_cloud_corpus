package com.springboot.tools.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.tools.web.exception.MyException;
@Controller
public class HelloController {
	@ResponseBody
	@RequestMapping("/hello")
	public String index() {
		return "hello world";
	}

	@RequestMapping("/")
	public String index(ModelMap map) {
		map.addAttribute("host","http://blog.learn.springboot");
		return "index";
	}
	@RequestMapping("/exp")
	public String exception() throws Exception{
		throw new Exception("发生错误");
	}
	@RequestMapping("/json")
	public String json() throws MyException{
		throw new MyException("发生错误2");
	}
}
