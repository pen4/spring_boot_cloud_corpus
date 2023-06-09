package com.example.springboot.tdengine.demo.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.tdengine.demo.mybatisplus.entity.User;
import com.example.springboot.tdengine.demo.mybatisplus.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
