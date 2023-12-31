package com.springboot.rpc.ssl;

import io.grpc.Server;

import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class TlsServer {

    @SneakyThrows
    public static void main(String[] args) {
        setLogger("io.grpc");

        // 初始化 SSL 上下文
        File keyCertChainFile = new File("grpc-exe-demo/src/main/resources/cert/server.pem");
        File keyFile = new File("grpc-exe-demo/src/main/resources/cert/server.key");

        SslContext sslContext = GrpcSslContexts.forServer(keyCertChainFile, keyFile)
                                               .clientAuth(ClientAuth.OPTIONAL)
                                               .build();

        // 构建 Server
        Server server = NettyServerBuilder.forAddress(new InetSocketAddress(9090))
                                          // 添加服务
                                          .addService(new HelloServiceImpl())
                                          .sslContext(sslContext)
                                          .build();

        // 启动 Server
        server.start();
        log.info("服务端启动成功");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        // 保持运行
        server.awaitTermination();
    }

    private static void setLogger(String className) {
        Logger logger = Logger.getLogger(className);
        logger.setLevel(Level.ALL);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }
}

@Slf4j
class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloMessage request, StreamObserver<HelloResponse> responseObserver) {
        log.info("收到客户端请求: " + request.getMessage());

        // 构建响应
        HelloResponse response = HelloResponse.newBuilder()
                                              .setMessage("Hello " + request.getMessage())
                                              .build();

        // 发送响应
        responseObserver.onNext(response);
        // 结束请求
        responseObserver.onCompleted();
    }
}
