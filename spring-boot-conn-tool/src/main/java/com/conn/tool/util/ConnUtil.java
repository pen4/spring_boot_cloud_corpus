package com.conn.tool.util;

import com.conn.tool.grpc.GrpcConnectTool;
import com.conn.tool.grpc.RpcCallParams;
import com.conn.tool.websockets.WebSocketClientConnectTool;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: User
 * @date: 2023/6/20
 * @Description:此类用于xxx
 */
public class ConnUtil {
    public static void conn(String url) {
        Preconditions.checkState(StringUtils.isNotBlank(url));
        try {
            if (url.startsWith("http")) {

            } else if (url.startsWith("https")) {

            } else if (url.startsWith("ws")) {
                WebSocketClientConnectTool.websocketConn(url);
            } else if (url.startsWith("grpc")) {
              /*  RpcCallParams rpcCallParams = new RpcCallParams();
                GrpcConnectTool.execute(rpcCallParams);*/
            } else if (url.startsWith("dubbo")) {

            }
        } catch (Exception e) {

        }

    }

}
