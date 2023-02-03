package com.springboot;


import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.client.IgniteClientFuture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IgniteApiTest {


    @Autowired
    IgniteClient client;

    @Test
    public void test000() {
        ClientCache<String, String> cache = client.cache("abc");
        String str = (String) cache.get("test");
        System.out.println(str);

    }

    @Test
    public void test001() throws ExecutionException, InterruptedException, TimeoutException {

        ClientCache clientCache = client.getOrCreateCache("abc");
        IgniteClientFuture<String> future = clientCache.getAsync("test");
        String str = future.get(5, TimeUnit.SECONDS);
        System.out.println(str);

    }

    @Test
    public void test002() {
        ClientCache cache = client.getOrCreateCache("SQL_PUBLIC_TEST001");
        cache.put("name", "test001");
        cache.put("id", "1");
        cache.put("value", "001");
    }


    @Test
    public void test003() {

        Collection<String> caches = client.cacheNames();
        String res = String.join("", caches);
        System.out.println(res);
        //SQL_PUBLIC_TEST
    }

    @Test
    public void test004() {
        ClientCache clientCache = client.getOrCreateCache("PUBLIC");

        SqlFieldsQuery sql = new SqlFieldsQuery("select * from \"VEHICLE_DEVICE_DICT\""
              );

        // Iterate over the result set.
        List<Object> res = clientCache.query(sql).getAll();
        res.get(0);
    }


    @Test
    public void test005() {

        ClientCache clientCache = client.getOrCreateCache("PUBLIC");
        SqlFieldsQuery sql = new SqlFieldsQuery("INSERT INTO Person (id, name, city_id) VALUES(?, ?, ?)");
        sql.setArgs("6", "Richard Miles", "2");
        // Iterate over the result set.
        List<?> list = clientCache.query(sql).getAll();
        System.out.println(list);
    }


    @Test
    public void test006() {

        ClientCache clientCache = client.getOrCreateCache("PUBLIC");
        SqlFieldsQuery sql = new SqlFieldsQuery("merge into  Person  (id, name, city_id)  " + " values (6,'John',3),(7,'John',3)");
        // Iterate over the result set.
        List<?> list = clientCache.query(sql).getAll();
        System.out.println(list);
    }


    @Test
    public void test007() {
        SqlFieldsQuery sql = new SqlFieldsQuery("SELECT  NAME FROM VEHICLE ").setSchema("VehicleCache");
        // Iterate over the result set.
        List<?> list = client.query(sql).getAll();
        System.out.println(list);
    }
    @Test
    public void test008() {

        // Iterate over the result set.

       // List<?> list = client.query(sql).getAll();
        client.destroyCache("PersonCache");
     //   System.out.println(list);
    }

}
