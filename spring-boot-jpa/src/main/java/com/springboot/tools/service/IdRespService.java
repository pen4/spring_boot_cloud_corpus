package com.springboot.tools.service;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: User
 * @date: 2023/5/6
 * @Description:此类用于xxx
 */
@Service
public class IdRespService {


    private AtomicLong count = new AtomicLong(0l);
    private final LinkedBlockingQueue queue = new LinkedBlockingQueue();
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            ThreadGroup group = new ThreadGroup("copy-thread-group");
            return new Thread(group, r, "copy-" + threadNumber.getAndIncrement());
        }
    };
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(17, 20, 3, TimeUnit.MINUTES, queue, threadFactory);
    private final CountDownLatch countDownLatch = new CountDownLatch(419);

    @Autowired
    CopyCommandService copyCommandService;

    public void copyCommand() throws InterruptedException, ExecutionException {
        copyCommandService.setCountDownLatch(countDownLatch);
        List<CopyCommandService> all = Lists.newArrayList();
        for (int i = 1; i <= 420; i++) {
            String path = "D:\\logs4\\" + i + ".txt";
            copyCommandService.setPath(path);
            all.add(copyCommandService);
            Future<Integer> res= threadPoolExecutor.submit(copyCommandService);
             count.getAndAdd((Integer) res.get());
        }
       /* List<Future<Integer>> res = threadPoolExecutor.invokeAll(all);
        for (Future<Integer> future : res) {
            count.getAndAdd(future.get());
        }*/
        countDownLatch.await();

        System.out.println(count + ">>00000000000000000000");

    }

}
