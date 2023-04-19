package com.kxd.sentinel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: User
 * @date: 2023/3/20
 * @Description:此类用于构造器方法
 */
@Service
@Slf4j
public class TestConstructService {
    public  void sayHello(){
      log.info("test lookup say hello ");
    }
}
