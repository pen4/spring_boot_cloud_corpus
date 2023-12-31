package com.springboot.tools;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Lists;
import com.springboot.tools.entity.IdInfo;
import com.springboot.tools.repo.IdResp;
import com.springboot.tools.service.IdRespService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: User
 * @date: 2023/4/26
 * @Description:此类用于xxx
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdTest {

    @Autowired
    IdResp idResp;

    @Test
    public void testId() throws InterruptedException {

        StopWatch start = new StopWatch();
        start.start();

        ThreadPoolExecutor executor = ThreadUtil.newExecutor(32, 45);
        for (int j = 0; j < 1; j++) {
            executor.execute(new Task());
        }
        start.stop();
        System.out.println(start.getTotalTimeSeconds());
        Thread.currentThread().join();

    }

    class Task implements Runnable {
        public Task() {
        }

        @Override
        public void run() {
            List<IdInfo> list = new ArrayList<>();
            for (long i = 0; i < 5000000; i++) {
                list.add(new IdInfo());
            }
            List<List<IdInfo>> lists1 = Lists.partition(list, 20000);
            for (List<IdInfo> par2 : lists1) {//8000
                idResp.saveAllAndFlush(par2);//5000
            }
            System.out.println("一批完成");
            list = null;
        }
    }

    @Autowired
    IdRespService idRespService;

    @Test
    public void testCopy() throws InterruptedException, ExecutionException {
        idRespService.copyCommand();
    }
}
