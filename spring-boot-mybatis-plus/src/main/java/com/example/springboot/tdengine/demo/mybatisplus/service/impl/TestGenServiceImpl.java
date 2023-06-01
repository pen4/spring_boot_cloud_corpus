package com.example.springboot.tdengine.demo.mybatisplus.service.impl;


import com.example.springboot.tdengine.demo.mybatisplus.mapper.TestGenMapper;
import com.example.springboot.tdengine.demo.mybatisplus.service.ITestGenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.tdengine.demo.mybatisplus.entity.TestGen;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kxd
 * @since 2023-06-01
 */
@Service
public class TestGenServiceImpl extends ServiceImpl<TestGenMapper, TestGen> implements ITestGenService {

}
