package com.springboot.tools.web.service;

public interface UserService {
	void create(String name, Integer age);

	void deleteByName(String name);

	Integer getAllUsers();

	void deleteAllUsers();
}
