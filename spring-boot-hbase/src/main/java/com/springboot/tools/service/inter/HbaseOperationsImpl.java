package com.springboot.tools.service.inter;
import com.springboot.tools.config.HbaseSystemException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kxd
 * 操作实现类
 */
public class HbaseOperationsImpl implements HbaseOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(HbaseOperationsImpl.class);

    private Configuration configuration;

    private volatile Connection connection;

    public HbaseOperationsImpl(Configuration configuration) {
        this.setConfiguration(configuration);
        Assert.notNull(configuration, " a valid configuration is required");
    }

    @Override
    public <T> T execute(String tableName, TableCallback<T> action) {
        Assert.notNull(action, "Callback object must not be null");
        Assert.notNull(tableName, "No table specified");
        StopWatch sw = new StopWatch();
        sw.start();
        Table table = null;
        try {
            table = this.getConnection().getTable(TableName.valueOf(tableName));
            return action.doInTable(table);
        } catch (Throwable throwable) {
            throw new HbaseSystemException(throwable);
        } finally {
            if (null != table) {
                try {
                    table.close();
                    sw.stop();
                } catch (IOException e) {
                    LOGGER.error("hbase资源释放失败");
                }
            }
        }
    }

    @Override
    public <T> List<T> find(String tableName, String family, final RowMapper<T> action) {
        Scan scan = new Scan();
        scan.setCaching(5000);
        scan.addFamily(Bytes.toBytes(family));
        return this.find(tableName, scan, action);
    }

    @Override
    public <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> action) {
        Scan scan = new Scan();
        scan.setCaching(5000);
        scan.addFamily(Bytes.toBytes(family));
        scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        return this.find(tableName, scan, action);
    }

    public <T> List<T> findByPage(String tableName, String family, String startRow, String endRow, int pageSize, final RowMapper<T> action) {
        Scan scan = new Scan();
        Filter page = new PageFilter(pageSize);
        scan.addFamily(Bytes.toBytes(family));
        scan.setFilter(page);
        scan.withStartRow(Bytes.toBytes(startRow)).withStopRow(Bytes.toBytes(endRow));
        scan.setCacheBlocks(true);
        return this.find(tableName, scan, action);
    }

    /**
     * 使用了一个substringComparator 减少了查询误差
     * <p>
     * 如何实现列表组件
     * 聪明的你一定已经想到解决方案了吧，那就是把lastRowkey记录到Session中或者是写到页面上，然后请求下一页的时候把这个rowkey传给服务端用于构建下一次Scan的startRowkey。
     *
     * @param tableName
     * @param family
     * @param startRow
     * @param endRow
     * @param pageSize
     * @param action
     * @param subString
     * @param <T>
     * @return
     */
    public <T> List<T> findByPage2(String tableName, String family, String startRow, String endRow, long pageSize, final RowMapper<T> action, String subString) {
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));
        scan.withStartRow(Bytes.toBytes(startRow)).withStopRow(Bytes.toBytes(endRow));
        FilterList filters = new FilterList();
        RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(subString));
        PageFilter page = new PageFilter(pageSize);
        filters.addFilter(rowFilter);
        filters.addFilter(page);
        scan.setFilter(filters);
        scan.setCacheBlocks(true);
        return this.find(tableName, scan, action);
    }

    /**
     * 多段查询start end row 保持 数量一致
     *
     * @param tableName tableName
     * @param family    family
     * @param startRow  startRow与 endRow 数量一致
     * @param endRow    endRow
     * @param action    action
     * @param <T>       T
     * @return T
     */
    public <T> List<T> findByMultiRowRange(String tableName, String family, List<String> startRow, List<String> endRow, final RowMapper<T> action) {
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(family));

        ColumnRangeFilter columnRangeFilter=new ColumnRangeFilter(Bytes.toBytes("f1"),true,Bytes.toBytes("f2"),true);
        FilterList list=new FilterList();
        list.addFilter(columnRangeFilter);
        if (CollectionUtils.isNotEmpty(startRow)) {
            List<MultiRowRangeFilter.RowRange> ranges = new ArrayList<>();
            for (int i = 0; i < startRow.size(); i++) {
                MultiRowRangeFilter.RowRange range = new MultiRowRangeFilter.RowRange(startRow.get(i), true, endRow.get(i), false);
                ranges.add(range);
            }
            MultiRowRangeFilter multiRowRangeFilter = new MultiRowRangeFilter(ranges);
            list.addFilter(multiRowRangeFilter);

        }
        scan.setFilter(list);
        scan.setCacheBlocks(true);
        return this.find(tableName, scan, action);
    }


    @Override
    public <T> List<T> find(String tableName, final Scan scan, final RowMapper<T> action) {
        return this.execute(tableName, new TableCallback<List<T>>() {
            @Override
            public List<T> doInTable(Table table) throws Throwable {
                int caching = scan.getCaching();
                // 如果caching未设置(默认是-1)，将默认配置成1000
                if (caching == -1) {
                    scan.setCaching(1000);
                }
                try (ResultScanner scanner = table.getScanner(scan)) {
                    List<T> rs = new ArrayList<T>();
                    int rowNum = 0;
                    for (Result result : scanner) {
                        rs.add(action.mapRow(result, rowNum++));
                    }
                    return rs;
                }
            }
        });
    }

    @Override
    public <T> T get(String tableName, String rowName, final RowMapper<T> mapper) {
        return this.get(tableName, rowName, null, null, mapper);
    }

    @Override
    public <T> T get(String tableName, String rowName, String familyName, final RowMapper<T> mapper) {
        return this.get(tableName, rowName, familyName, null, mapper);
    }

    @Override
    public <T> T get(String tableName, final String rowName, final String familyName, final String qualifier, final RowMapper<T> mapper) {
        return this.execute(tableName, new TableCallback<T>() {
            @Override
            public T doInTable(Table table) throws Throwable {
                Get get = new Get(Bytes.toBytes(rowName));
                if (StringUtils.isNotBlank(familyName)) {
                    byte[] family = Bytes.toBytes(familyName);
                    if (StringUtils.isNotBlank(qualifier)) {
                        get.addColumn(family, Bytes.toBytes(qualifier));
                    } else {
                        get.addFamily(family);
                    }
                }
                Result result = table.get(get);
                return mapper.mapRow(result, 0);
            }
        });
    }

    @Override
    public void execute(String tableName, MutatorCallback action) {
        Assert.notNull(action, "Callback object must not be null");
        Assert.notNull(tableName, "No table specified");

        StopWatch sw = new StopWatch();
        sw.start();
        BufferedMutator mutator = null;
        try {
            BufferedMutatorParams mutatorParams = new BufferedMutatorParams(TableName.valueOf(tableName));
            mutator = this.getConnection().getBufferedMutator(mutatorParams.writeBufferSize(3 * 1024 * 1024));
            action.doInMutator(mutator);
        } catch (Throwable throwable) {
            sw.stop();
            throw new HbaseSystemException(throwable);
        } finally {
            if (null != mutator) {
                try {
                    mutator.flush();
                    mutator.close();
                    sw.stop();
                } catch (IOException e) {
                    LOGGER.error("hbase mutator资源释放失败");
                }
            }
        }
    }

    @Override
    public void saveOrUpdate(String tableName, final Mutation mutation) {
        this.execute(tableName, new MutatorCallback() {
            @Override
            public void doInMutator(BufferedMutator mutator) throws Throwable {
                mutator.mutate(mutation);
            }
        });
    }

    @Override
    public void saveOrUpdates(String tableName, final List<Mutation> mutations) {
        this.execute(tableName, new MutatorCallback() {
            @Override
            public void doInMutator(BufferedMutator mutator) throws Throwable {
                mutator.mutate(mutations);
            }
        });
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        if (null == this.connection) {
            synchronized (this) {
                if (null == this.connection) {
                    try {
                        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(200, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                        // init pool
                        poolExecutor.prestartCoreThread();
                        this.connection = ConnectionFactory.createConnection(configuration, poolExecutor);
                    } catch (IOException e) {
                        LOGGER.error("hbase connection资源池创建失败");
                    }
                }
            }
        }
        return this.connection;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
