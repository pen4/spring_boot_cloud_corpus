package com.springboot.tools.test;

import com.springboot.tools.web.domain.BlogProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired(required = false)
	private BlogProperties blogProperties;

	@Test
	public void contextLoads() {
		System.out.println(blogProperties.getName());
		System.out.println(blogProperties.getTitle());
		System.out.println(blogProperties.getContent());
		System.out.println(blogProperties.getContent());
	}

}
