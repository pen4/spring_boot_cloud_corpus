package com.springboot.tools.test.multiResource;

import com.springboot.tools.web.SpringBootToolsApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootToolsApplication.class)
public class ApplicationTests {

	@Autowired
	@Qualifier("primaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate1;

	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	protected JdbcTemplate jdbcTemplate2;

	@Before
	public void setUp() {
		jdbcTemplate1.update("delete from user");
		jdbcTemplate2.update("delete from user");
	}

	@Test
	public void test() {
		jdbcTemplate1.update("insert into user (name,age) values (?,?)", "aaa", 20);
		jdbcTemplate1.update("insert into user (name,age) values (?,?)", "bbb", 30);
		jdbcTemplate2.update("insert into user (id,name,age) values (?,?,?)", 1, "aaa", 40);
	}
}
