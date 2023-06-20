package com.conn.tool.grpc;

public class RpcCallParams {
    private final String methodSymbol;
    private final String requestContent;
    private final String host;
    private final int port;

    /**
     * @param methodSymbol
     * @param requestContent
     * @param host
     * @param port
     *
     */
    public RpcCallParams(String methodSymbol, String requestContent, String host, int port) {
        this.methodSymbol = methodSymbol;
        this.requestContent = requestContent;
        this.host = host;
        this.port = port;
    }

    public String getMethodSymbol() {
        return methodSymbol;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
