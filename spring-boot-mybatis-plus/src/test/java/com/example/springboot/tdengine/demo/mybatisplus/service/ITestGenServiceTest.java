package com.example.springboot.tdengine.demo.mybatisplus.service;

import com.example.springboot.tdengine.demo.mybatisplus.entity.TestGen;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ITestGenServiceTest {
    @Autowired
    ITestGenService testGenService;

    @Test
    public void add() {
        TestGen gen = new TestGen();
        gen.setAge(10);
        gen.setName("测试");
        gen.setId(1l);
        Boolean res = testGenService.save(gen);
        Assert.assertTrue(res);
    }

    @Test
    public void saveBatch() {
        TestGen gen = new TestGen();
        gen.setAge(11);
        gen.setName("测试3");

        TestGen gen2 = new TestGen();
        gen2.setAge(12);
        gen2.setName("测试");
        List<TestGen> list = new ArrayList<>();
        list.add(gen2);
        list.add(gen);
        testGenService.saveBatch(list);

    }

}