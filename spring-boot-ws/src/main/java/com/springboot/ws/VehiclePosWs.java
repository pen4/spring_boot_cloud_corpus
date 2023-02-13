package com.springboot.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: kangxd
 * @date:  2022/10/26
 * @Description: 此类用于位置信息socket
 */
@Component
@ServerEndpoint(value = "/ws/v-pos")
@Slf4j
public class VehiclePosWs {

    /**
     * ws中session连接
     */
    private Session session;

    private final LongAdder atomicInteger=new LongAdder();



    /**
     * 存放ws对象的集合
     */
    private static final CopyOnWriteArraySet<VehiclePosWs> vehiclePosWsSet = new CopyOnWriteArraySet<>();

    /**
     *
     * 前端发送连接请求
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        vehiclePosWsSet.add(this);
        addOnlineCount();
        try {
            sendMessage("位置信息连接已成功建立");
        } catch (Exception e) {
            log.error("位置信息通信异常！", e);
        }
        log.info("【websocket位置信息】有新的连接, 总数:{}", vehiclePosWsSet.size());
    }


    /**
     * 关闭socket请求
     */
    @OnClose
    public void onClose() {
        vehiclePosWsSet.remove(this);
        subOnlineCount();
        log.info("【websocket消息位置信息】连接断开, 总数:{}", vehiclePosWsSet.size());
    }

    /**
     * 接收到发送来的消息
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息位置信息】收到客户端发来的消息:{}", message);
    }

    /**
     * 主动向客户端发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        log.info("【websocket消息位置信息】广播消息, message={}", message);
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("发送消息异常", e);
        }
    }

    private void addOnlineCount() {
        atomicInteger.increment();
    }

    private void subOnlineCount() {
        atomicInteger.decrement();
    }

    public static Set<VehiclePosWs> getWebSocketSet() {
        return vehiclePosWsSet;
    }

}