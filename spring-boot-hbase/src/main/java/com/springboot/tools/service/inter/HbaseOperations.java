package com.springboot.tools.service.inter;

import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Scan;

import java.util.List;

/**
 * @author kangxd
 * 操作api
 */

public interface HbaseOperations {

    /**
     * 执行指定操作
     * @param tableName 表
     * @param mapper  callback
     * @return T
     * @param <T> 返回格式
     */
    <T> T execute(String tableName, TableCallback<T> mapper);

    /**
     * scan返回结果列表
     * @param tableName 表
     * @param family 列族
     * @param mapper row处理回调
     * @return  结果列表
     * @param <T> 类型
     */
    <T> List<T> find(String tableName, String family, final RowMapper<T> mapper);

    /**
     * 返回scan列表
     * @param tableName 表
     * @param family 列族
     * @param qualifier 列
     * @param mapper row mapper
     * @return 列表
     * @param <T> 类型
     */
    <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> mapper);

    /**
     * 执行scan方法返回列表
     * @param tableName 表
     * @param scan scan
     * @param mapper mapper
     * @return 列表
     * @param <T>  T
     */
    <T> List<T> find(String tableName, final Scan scan, final RowMapper<T> mapper);


    <T> List<T> findByPage(String tableName, String family, String startRow, String endRow,int pageSize, final RowMapper<T> action) ;
    /**
     * 返回指定row key数据
     * @param tableName tableName
     * @param rowName row key
     * @param mapper mapper
     * @return T
     * @param <T> T
     */
    <T> T get(String tableName, String rowName, final RowMapper<T> mapper);

    /**
     * 返回指定 rowName  familyName 行数据
     * @param tableName table
     * @param rowName row key
     * @param familyName family
     * @param mapper mapper
     * @return T
     * @param <T> T
     */
    <T> T get(String tableName, String rowName, String familyName, final RowMapper<T> mapper);

    /**
     * 返回指定 rowName familyName qualifier的数据
     * @param tableName tableName
     * @param rowName rowName
     * @param familyName familyName
     * @param qualifier qualifier
     * @param mapper mapper
     * @return T
     * @param <T> T
     */

    <T> T get(String tableName, final String rowName, final String familyName, final String qualifier, final RowMapper<T> mapper);

    /**
     * 执行put update or delete
     * @param tableName tableName
     * @param action action
     */
    void execute(String tableName, MutatorCallback action);

    /**
     * 更新
     * @param tableName tableName
     * @param mutation mutation
     */
    void saveOrUpdate(String tableName, Mutation mutation);

    /**
     * 更新批量
     * @param tableName tableName
     * @param mutations mutations
     */
    void saveOrUpdates(String tableName, List<Mutation> mutations);
}