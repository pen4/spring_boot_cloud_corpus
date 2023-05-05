package com.springboot.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Lists;
import com.springboot.tools.entity.IdInfo;
import com.springboot.tools.repo.IdResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: User
 * @date: 2023/4/26
 * @Description:此类用于xxx
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdTestCopyCommand {
    CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(3);

    @Test
    public void testId() throws InterruptedException {

        StopWatch start = new StopWatch();
        start.start();

        ThreadPoolExecutor executor = ThreadUtil.newExecutor(32, 45);
        for (int j = 2; j < 398; j++) {
            executor.execute(new Task(j));
        }
        start.stop();
        System.out.println(start.getTotalTimeSeconds());
        countDownLatch.await();

    }

    class Task implements Runnable {

        private static final int size = 5000000;

        private volatile Integer index;

        public Task(int index) {
            this.index = index;
            this.total = index * size;

            this.start = (index - 1) * size + 1;
        }

        private volatile long total;
        private volatile long start;

        @Override
        public void run() {
            List<Long> nums = Lists.newArrayList();
            for (long i = start; i <= total; i++) {
                nums.add(i);
            }
            List<List<Long>> partition = Lists.partition(nums, 10000);

            for (List<Long> par : partition) {
                FileUtil.appendLines(par, "D:\\logs1\\6.txt", Charset.defaultCharset());
            }

            System.out.println("一批完成");
        }



    }
}
