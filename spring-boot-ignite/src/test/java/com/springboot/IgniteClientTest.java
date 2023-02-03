package com.springboot;

import com.springboot.domain.Person;
import org.apache.ignite.IgniteBinary;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.Query;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.client.*;
import org.apache.ignite.cluster.ClusterState;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.cache.Cache;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class IgniteClientTest {


    @Autowired
    IgniteClient client;

    @Test
    void test() {
        ClientCacheConfiguration cacheCfg = new ClientCacheConfiguration().setName("References").setCacheMode(CacheMode.REPLICATED).setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);

        ClientCache<Integer, String> cache = client.getOrCreateCache(cacheCfg);

        Map<Integer, String> data = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toMap(i -> i, Object::toString));

        cache.putAll(data);

        assert !cache.replace(1, "2", "3");
        assert "1".equals(cache.get(1));
        assert cache.replace(1, "1", "3");
        assert "3".equals(cache.get(1));

        cache.put(101, "101");

        cache.removeAll(data.keySet());
        assert cache.size() == 1;
        assert "101".equals(cache.get(101));

        cache.removeAll();
        assert 0 == cache.size();
    }

    @Test
    public void test02() {
        ClientCache<Integer, Person> personCache = client.getOrCreateCache("personCache");

        Query<Cache.Entry<Integer, Person>> qry = new ScanQuery<>((i, p) -> p.getName().contains("Smith"));


        try (QueryCursor<Cache.Entry<Integer, Person>> cur = personCache.query(qry)) {
            for (Cache.Entry<Integer, Person> entry : cur) {
                // Process the entry ...
                System.out.println(entry.getValue().getName() + entry.getKey());
            }
        }

    }


    @Test
    public void test03() {
        ClientCache<Integer, String> cache = client.getOrCreateCache("my_transactional_cache");

        ClientTransactions tx = client.transactions();

        try (ClientTransaction t = tx.txStart()) {
            cache.put(1, "new value");
            t.commit();
        }
    }

    @Test
    public void test04() {
        IgniteBinary binary = client.binary();

        BinaryObject val = binary.builder("Person").setField("id", 1, int.class).setField("name", "Joe", String.class).build();

        ClientCache<Integer, BinaryObject> cache = client.getOrCreateCache("persons").withKeepBinary();

        cache.put(1, val);

        BinaryObject value = cache.get(1);

    }

    @Test
    void test05() throws ExecutionException, InterruptedException {
        ClientCache<Integer, String> cache = client.getOrCreateCache("cache");

        IgniteClientFuture<Void> putFut = cache.putAsync(1, "hello");
        putFut.get(); // Blocking wait.

        IgniteClientFuture<String> getFut = cache.getAsync(1);
        getFut.thenAccept(val -> System.out.println(val)); // Non-blocking
    }

    @Test
    void test06() {

        String personId = "007";
        // Create a regular Person object and put it to the cache.
        Person person = Person.buildPerson(personId);
        client.cache("myCache").put(personId, person);

        // Get an instance of binary-enabled cache.
        ClientCache<String, BinaryObject> binaryCache = client.cache("myCache").withKeepBinary();

        // Get the above person object in the BinaryObject format.
        BinaryObject binaryPerson = binaryCache.get(personId);
        Class clazzName= binaryPerson.getClass();


    }

    @Test
    void test08(){
      ClusterState state= client.cluster().state();
      state.name();
      state.active();
    }
}
