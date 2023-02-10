package com.springboot.tools.service.inter;

import org.apache.hadoop.hbase.client.Table;

/**
 * @author kxd
 * table操作回调方法
 * @param <T>
 */
public interface TableCallback<T> {

    /**
     * table 操作
     * @param table Table
     * @return  T
     * @throws Throwable
     */
    T doInTable(Table table) throws Throwable;
}