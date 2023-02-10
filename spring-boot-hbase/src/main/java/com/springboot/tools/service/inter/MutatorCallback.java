package com.springboot.tools.service.inter;

import org.apache.hadoop.hbase.client.BufferedMutator;

/**
 * @author kxd
 * 操作回调方法
 */
public interface MutatorCallback {

    /**
     * 使用mutator api to update put and delete
     * @param mutator
     * @throws Throwable
     */
    void doInMutator(BufferedMutator mutator) throws Throwable;
}
