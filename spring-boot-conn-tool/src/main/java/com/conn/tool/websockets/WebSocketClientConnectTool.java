package com.conn.tool.websockets;

import com.conn.tool.domain.CodeMsg;
import com.conn.tool.util.SSLSocketFactoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;

import javax.net.ssl.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

/**
 * @author: User
 * @date: 2023/6/12
 * @Description:此类用于xxx
 */
@Slf4j
public class WebSocketClientConnectTool {

    @Test
    public void test() throws InterruptedException {
        String url = "wss://36.138.127.97:30980/backend/ws/task-management/ws/v-pos";
        CodeMsg res = null;
        try {
            res = websocketConn(url);
            log.info("test end,code:{},msg:{}", res.getCode(), res.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("连接失败");
        }


    }

    /**
     * String url = "wss://36.138.127.97:30980/backend/ws/task-management/ws/v-pos";
     *
     * @param url
     * @throws URISyntaxException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static CodeMsg websocketConn(String url) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, InterruptedException {
        SSLSocketFactory factory = SSLSocketFactoryUtil.getSslSocketFactory();
        CodeMsg codeMsg = new CodeMsg();
        //ws://localhost:18060/ws/v-pos
        WebSocketClient webSocketClient = new WebSocketClient(new URI(url)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                short httpStatusMessage = handshakedata.getHttpStatus();
                codeMsg.setCode(httpStatusMessage);
                codeMsg.setMsg("open");
            }

            @Override
            public void onMessage(String s) {

            }


            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("close code: " + code);
                System.out.println("close reason: " + reason);
                System.out.println("close remote: " + remote);
            }

            @Override
            public void onError(Exception ex) {
                codeMsg.setCode(-1);
                codeMsg.setMsg(ex.getLocalizedMessage());
                throw new RuntimeException(ex);

            }
        };
        if (StringUtils.contains(url, "wss:")) {
            webSocketClient.setSocketFactory(factory);
        }
        webSocketClient.connect();
        ThreadUtils.sleep(Duration.ofSeconds(2));
        webSocketClient.close();
        return codeMsg;
    }

}
