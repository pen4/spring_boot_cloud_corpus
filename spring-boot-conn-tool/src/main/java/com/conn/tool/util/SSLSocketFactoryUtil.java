package com.conn.tool.util;

import javax.net.ssl.*;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author: User
 * @date: 2023/6/12
 * @Description:此类用于xxx
 */
public class SSLSocketFactoryUtil {


    public static SSLSocketFactory getSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, getTrustManagers(), null); // will use java's default key and trust store which is sufficient unless you deal with self-signed certificates

        SSLSocketFactory factory = sslContext
                .getSocketFactory();// (SSLSocketFactory) SSLSocketFactory.getDefault();
        return factory;
    }


    private static TrustManager[] getTrustManagers() {
        return new TrustManager[]{
                new X509ExtendedTrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

                    }
                }
        };
    }
}
