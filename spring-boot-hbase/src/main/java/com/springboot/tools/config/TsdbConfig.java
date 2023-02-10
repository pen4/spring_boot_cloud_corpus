package com.springboot.tools.config;

import org.apache.http.nio.reactor.IOReactorException;
import org.opentsdb.client.OpenTSDBClient;
import org.opentsdb.client.OpenTSDBClientFactory;
import org.opentsdb.client.OpenTSDBConfig;
import org.opentsdb.client.bean.request.Point;
import org.opentsdb.client.bean.response.DetailResult;
import org.opentsdb.client.http.callback.BatchPutHttpResponseCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class TsdbConfig {

    @Bean
    public OpenTSDBClient client() {
        OpenTSDBConfig config = OpenTSDBConfig.address("http://192.168.31.233", 4242)
                // http连接池大小，默认100
                .httpConnectionPool(100)
                // http请求超时时间，默认100s
                .httpConnectTimeout(100)
                // 异步写入数据时，每次http提交的数据条数，默认50
                .batchPutSize(100)
                // 异步写入数据中，内部有一个队列，默认队列大小20000
                .batchPutBufferSize(20000)
                // 异步写入等待时间，如果距离上一次请求超多300ms，且有数据，则直接提交
                .batchPutTimeLimit(300)
                // 当确认这个client只用于查询时设置，可不创建内部队列从而提高效率

                // 每批数据提交完成后回调
                .batchPutCallBack(new BatchPutHttpResponseCallback.BatchPutCallBack() {
                    @Override
                    public void response(List<Point> points, DetailResult result) {
                        // 在请求完成并且response code成功时回调
                    }

                    @Override
                    public void responseError(List<Point> points, DetailResult result) {
                        // 在response code失败时回调
                    }

                    @Override
                    public void failed(List<Point> points, Exception e) {
                        // 在发生错误是回调
                    }
                }).config();
        OpenTSDBClient client = null;
        try {
            client = OpenTSDBClientFactory.connect(config);
        } catch (IOReactorException e) {
            // 优雅关闭连接，会等待所有异步操作完成
            try {
                client.gracefulClose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        return client;
    }
}
