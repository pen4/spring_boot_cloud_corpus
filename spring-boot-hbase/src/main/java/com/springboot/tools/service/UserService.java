package com.springboot.tools.service;

import com.springboot.tools.model.UserModel;

public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);
}
