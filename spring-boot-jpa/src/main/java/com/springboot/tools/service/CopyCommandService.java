package com.springboot.tools.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author: User
 * @date: 2023/5/6
 * @Description:此类用于xxx
 */
@Service
@Slf4j
public class CopyCommandService implements Callable<Integer> {
    @PersistenceUnit
    private EntityManagerFactory factory;
    private static volatile String path;


    private CountDownLatch countDownLatch;

    @Override
    public Integer call() throws Exception {
        return exec(path);
    }


    public int exec(String path) {
        String sqlString = "COPY id_info FROM '" + path + "'";
        EntityManager entityManager = factory.createEntityManager();
        Query copyQuery = entityManager.createNativeQuery(sqlString);
        copyQuery.setFlushMode(FlushModeType.COMMIT);
        int res = 0;
        try {
            entityManager.getTransaction().begin();
            res = copyQuery.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("导入文件{},发生异常{}", path, e);
        } finally {
            countDownLatch.countDown();
            entityManager.close();
        }
        log.info("完成了任务写入文件,{}", path);
        return res;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
