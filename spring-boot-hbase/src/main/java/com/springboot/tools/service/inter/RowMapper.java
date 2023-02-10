package com.springboot.tools.service.inter;

import org.apache.hadoop.hbase.client.Result;


/**
 * @author kangxd
 * row 回调处理方法
 */
public interface RowMapper<T> {

    T mapRow(Result result, int rowNum) throws Exception;
}
