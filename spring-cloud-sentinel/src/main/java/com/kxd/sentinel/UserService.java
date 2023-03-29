package com.kxd.sentinel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserService  {

	@Autowired
	private static OrderService orderService;

	public void test() {
		System.out.println("test123");
	}

}