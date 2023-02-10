package com.springboot.tools.service;

import com.springboot.tools.config.HBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@DependsOn("HBaseConfig")
public class HBaseAsyncService {

    @Resource
    private HBaseConfig config;

    private static AsyncAdmin asyncAdmin = null;

    public static Configuration conf = null;

    private static AsyncConnection asyncConnection = null;

    @PostConstruct
    private void init() {
        if (asyncConnection != null) {
            return;
        }

        CompletableFuture<AsyncConnection> completableFuture = ConnectionFactory.createAsyncConnection(config.configuration());
        completableFuture.whenComplete((c, e) -> {
            asyncConnection = c;
            asyncAdmin = c.getAdmin();
        });

    }

    /**
     * 判断表是否已经存在，这里使用间接的方式来实现
     *
     * @param tableName 表名
     * @return boolean
     * @throws IOException io异常
     */
    public boolean tableExists(String tableName) throws ExecutionException, InterruptedException {
        CompletableFuture<List<TableName>> cf = asyncAdmin.listTableNames();
        List<TableName> tableNames = cf.get();
        if (tableNames != null && tableNames.size() > 0) {
            Optional<TableName> exist = tableNames.stream().filter(t->tableName.equals(t.getNameAsString())).findFirst();
            return exist.isPresent();
        }
        return false;
    }


    /**
     * 创建表
     * expression : create 'tableName','[Column Family 1]','[Column Family 2]'
     *
     * @param tableName      表名
     * @param columnFamilies 列族名
     * @throws IOException io异常
     */
    public void createTable(String tableName, byte[][] splitKeys, String... columnFamilies) throws IOException, ExecutionException, InterruptedException {
        TableName name = TableName.valueOf(tableName);
        boolean isExists = this.tableExists(tableName);

        if (isExists) {
            throw new TableExistsException(tableName + "is exists!");
        }

        TableDescriptorBuilder descriptorBuilder = TableDescriptorBuilder.newBuilder(name);
        List<ColumnFamilyDescriptor> columnFamilyList = new ArrayList<>();

        for (String columnFamily : columnFamilies) {
            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(columnFamily.getBytes())
                    .setCompressionType(Compression.Algorithm.SNAPPY).setBlockCacheEnabled(true).build();
            columnFamilyList.add(columnFamilyDescriptor);
        }
        descriptorBuilder.setColumnFamilies(columnFamilyList);
        TableDescriptor tableDescriptor = descriptorBuilder.build();
        if (null != splitKeys) {
            asyncAdmin.createTable(tableDescriptor, splitKeys);
        } else {
            asyncAdmin.createTable(tableDescriptor);
        }

    }

    /**
     * 插入或更新
     * expression : put <tableName>,<rowKey>,<family:column>,<value>,<timestamp>
     *
     * @param tableName    表名
     * @param rowKey       行id
     * @param columnFamily 列族名
     * @param column       列
     * @param value        值
     * @throws IOException io异常
     */
    public void insertOrUpdate(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException, ExecutionException, InterruptedException {
        this.insertOrUpdate(tableName, rowKey, columnFamily, new String[]{column}, new String[]{value});
    }

    /**
     * 插入或更新多个字段
     * expression : put <tableName>,<rowKey>,<family:column>,<value>,<timestamp>
     *
     * @param tableName    表名
     * @param rowKey       行id
     * @param columnFamily 列族名
     * @param columns      列
     * @param values       值
     */
    public void insertOrUpdate(String tableName, String rowKey, String columnFamily, String[] columns, String[] values) {
        AsyncTable<AdvancedScanResultConsumer> table = asyncConnection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int i = 0; i < columns.length; i++) {
             put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columns[i]), Bytes.toBytes(values[i]));
             table.put(put).join();
        }
    }

    /**
     * 删除行
     *
     * @param tableName 表名
     * @param rowKey    行唯一key
     */
    public void deleteRow(String tableName, String rowKey) {
        AsyncTable<?> table = asyncConnection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        table.delete(delete);
    }

    /**
     * 删除列族
     *
     * @param tableName    表名
     * @param rowKey       行id
     * @param columnFamily 列族名
     */
    public void deleteColumnFamily(String tableName, String rowKey, String columnFamily) {
        AsyncTable<?> table = asyncConnection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        delete.addFamily(Bytes.toBytes(columnFamily));
        table.delete(delete);
    }

    /**
     * 删除列
     * experssion : delete 'tableName','rowKey','columnFamily:column'
     *
     * @param tableName    表名
     * @param rowKey       行id
     * @param columnFamily 列族名
     * @param column       列名
     */
    public void deleteColumn(String tableName, String rowKey, String columnFamily, String column) {
        AsyncTable<?> table = asyncConnection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
        table.delete(delete);
    }

    /**
     * 删除表
     * expression : disable 'tableName' 之后 drop 'tableName'
     *
     * @param tableName 表名
     */
    public void deleteTable(String tableName) throws ExecutionException, InterruptedException {
        boolean isExists = this.tableExists(tableName);
        if (!isExists) {
            return;
        }
        TableName name = TableName.valueOf(tableName);
        asyncAdmin.disableTable(name);
        asyncAdmin.deleteTable(name);
    }

    /**
     * 获取值
     * expression : get 'tableName','rowkey','family:column'
     *
     * @param tableName 表名
     * @param rowKey    行id
     * @param family    列族名
     * @param column    列名
     * @return string
     */
    public String getValue(String tableName, String rowKey, String family, String column) {
        String value = "";
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family) || StringUtils.isBlank(rowKey) || StringUtils.isBlank(column)) {
            return null;
        }
        try {
            AsyncTable<?> asyncTable = asyncConnection.getTable(TableName.valueOf(tableName));
            Get g = new Get(rowKey.getBytes());
            g.addColumn(family.getBytes(), column.getBytes());
            CompletableFuture<Result> result = asyncTable.get(g);
            if (result.isDone()) {
                Result res = result.get();
                List<Cell> ceList = res.listCells();
                if (ceList != null && ceList.size() > 0) {
                    for (Cell cell : ceList) {
                        value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    }
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * 查询指定行
     * experssion : get 'tableName','rowKey'
     *
     * @param tableName 表名
     * @param rowKey    行id
     * @return String
     * @throws ExecutionException 异常
     */
    public String selectOneRow(String tableName, String rowKey) throws ExecutionException, InterruptedException {
        AsyncTable<?> table = asyncConnection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        CompletableFuture<Result> completableFuture = table.get(get);
        Result result = completableFuture.get();
        for (Cell cell : result.rawCells()) {
            String row = Bytes.toString(cell.getRowArray());
            String columnFamily = Bytes.toString(cell.getFamilyArray());
            String column = Bytes.toString(cell.getQualifierArray());
            String value = Bytes.toString(cell.getValueArray());
            // 可以通过反射封装成对象(列名和Java属性保持一致)
            System.out.println(row);
            System.out.println(columnFamily);
            System.out.println(column);
            System.out.println(value);
        }
        return null;
    }


    /**
     * 根据条件取出点位指定时间内的所有记录
     *
     * @param tableName   表名("OPC_TEST")
     * @param family      列簇名("OPC_COLUMNS")
     * @param column      列名("site")
     * @param value       值(采集点标识)
     * @param startMillis 开始时间毫秒值(建议传递当前时间前一小时的毫秒值，在保证查询效率的前提下获取到点位最新的记录
     * @return Map<String, Object>
     * @throws IOException io异常
     */
    public Map<String, Object> scanBatchOfTable(String tableName, String family, String[] column, String[] value, Long startMillis, Long endMillis) throws IOException {
        if (Objects.isNull(column) || column.length != value.length) {
            return null;
        }
        FilterList filterList = new FilterList();

        for (int i = 0; i < column.length; i++) {
            SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(column[i]), CompareOperator.EQUAL, Bytes.toBytes(value[i]));
            filterList.addFilter(filter);
        }

        AsyncTable<?> table = asyncConnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(filterList);
        if (startMillis != null && endMillis != null) {
            scan.setTimeRange(startMillis, endMillis);
        }
        return getStringStringMap(table, scan);
    }

    private Map<String, Object> getStringStringMap(AsyncTable<?> table, Scan scan) throws IOException {
        ResultScanner scanner = table.getScanner(scan);
        Map<String, Object> colMap = new HashMap<>();
        try {
            for (Result result : scanner) {
                for (Cell cell : result.listCells()) {
                    System.out.println(Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()));
                    colMap.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()), Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                }
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return colMap;
    }

    /**
     * 批量插入
     * @param tableName
     * @param puts
     */
    public void bulkPut(String tableName, List<Put> puts) {
        AsyncTable<AdvancedScanResultConsumer> table = asyncConnection.getTable(TableName.valueOf(tableName));
        CompletableFuture.allOf(table.put(puts).toArray(new CompletableFuture[0])).join();
    }

    /**
     * 单次插入
     * @param tableName
     * @param put
     */
    public void onePut(String tableName, Put put) {

        AsyncTable table = asyncConnection.getTable(TableName.valueOf(tableName));
        table.put(put);

    }




    /**
     * @param tableName 表
     * @param family    列族
     * @param startRow  开始行
     * @param endRow    结束行
     * @return Map<String, Object>
     * @throws IOException IOException
     */
    public Map<String, Object> scanTableStartEndRow(String tableName, String family, String startRow, String endRow) throws IOException {
        AsyncTable table = asyncConnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
        scan.withStartRow(Bytes.toBytes(startRow)).withStopRow(Bytes.toBytes(endRow));
        scan.setBatch(100);
        scan.setCacheBlocks(true);
        return getStringStringMap(table, scan);
    }

/*
    public int count(String tableName, String family, String startRow, String endRow) throws IOException {
        AsyncTable table = asyncConnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
        scan.withStartRow(Bytes.toBytes(startRow)).withStopRow(Bytes.toBytes(endRow));
        scan.setBatch(100);
        scan.setCacheBlocks(true);
        
    }*/
}
