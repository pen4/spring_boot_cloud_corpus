package com.springboot.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: User
 * @date: 2023/4/26
 * @Description:此类用于xxx
 */

public class IdTestCopyCommand2 {
    CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(3);

    @Test
    public void testId() throws InterruptedException {

        StopWatch start = new StopWatch();
        start.start();

        ThreadPoolExecutor executor = ThreadUtil.newExecutor(17, 32);
        for (int j = 401; j < 421; j++) {
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
            List<List<Long>> partition = Lists.partition(nums, 1000000);
            String f = "D:\\logs4\\" + index + ".txt";
            File file = FileUtil.newFile(f);
            for (int i = 0; i < partition.size(); i++) {
                FileUtil.appendLines(partition.get(i), file, Charset.defaultCharset());

            }
            System.out.println("一批完成");
        }


    }

    class TaskWrite implements Runnable {

        private volatile File file;
        private volatile List<Long> list;

        TaskWrite(File file, List<Long> list) {
            this.file = file;
            this.list = list;
        }

        @Override

        public void run() {

        }
    }
}
