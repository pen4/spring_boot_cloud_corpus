package com.springboot.tools;


import com.springboot.tools.service.HBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Slf4j
@SpringBootTest
public class HbaseTest {

    @Autowired
    HBaseService hBaseService;

    @Test
    public void createTable() throws IOException {
        hBaseService.createTable("test", "C1", "C2");
        hBaseService.insertOrUpdate("test", "1", "C1", "key1", "测试key1");
        hBaseService.insertOrUpdate("test", "2", "C2", "key2", "测试key2");
    }

    @Test
    public void getResultScanner() throws IOException {
        Map<String, Map<String, String>> test = hBaseService.getResultScanner("test");
        test.forEach((k, v) -> {
            log.info("key为:{},value为:{}", k, v);
        });
    }

    @Test
    public void deleteTable() throws IOException {
        hBaseService.deleteTable("test");
    }

    /**
     *      hBaseService.insertOrUpdate("my_ns:book", "1", "cf", "id", "1");
     *         hBaseService.insertOrUpdate("my_ns:book", "1", "cf", "book_name", "基督山伯爵");
     *         hBaseService.insertOrUpdate("my_ns:book", "1", "cf", "author", "zhang");
     *         hBaseService.insertOrUpdate("my_ns:book", "1", "cf", "price", "100");
     */
    @Test
    public void scanLatestOneRow() throws IOException {
      //  StopWatch stopWatch=new
        Map<String, String> res= hBaseService.scanLatestOneOfTable("my_ns:book", "cf", "author", "zhang", DateUtils.addDays(new Date(),-7).getTime() , new Date().getTime());

        for (Map.Entry<String,String> entry: res.entrySet()
             ) {
            System.out.println(entry.getKey()+entry.getValue());

        }

    }




    @Test
    public void createTablePreSplitHex() throws Exception{

        String startKey="1";
        String endKey="100000000";
        int numRegions=120;
        byte[][] splits=  getHexSplits(startKey,endKey,numRegions);
        //todo init
        HTableDescriptor table=null;
        Admin admin=null;
        createTable(admin,table,splits);

    }

    public static boolean createTable(Admin admin, HTableDescriptor table, byte[][] splits)
            throws IOException {
        try {
            admin.createTable( table, splits );
            return true;
        } catch (TableExistsException e) {
            log.info("table " + table.getNameAsString() + " already exists");
            // the table already exists...
            return false;
        }
    }

    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
        byte[][] splits = new byte[numRegions-1][];
        BigInteger lowestKey = new BigInteger(startKey, 16);
        BigInteger highestKey = new BigInteger(endKey, 16);
        BigInteger range = highestKey.subtract(lowestKey);
        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
        lowestKey = lowestKey.add(regionIncrement);
        for(int i=0; i < numRegions-1;i++) {
            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
            byte[] b = String.format("%016x", key).getBytes();
            splits[i] = b;
        }
        return splits;
    }

}
