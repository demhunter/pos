/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HttpClientUtils
 *
 * @author cc
 * @version 1.0, 16/9/14
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    public static String doGetSSL(String requestUrl, Map<String, Object> params) {
        requestUrl = appendRequestParams(requestUrl, params);
        return handleGetSSL(requestUrl);
    }

    public static String doGetSSL(String requestUrlPattern, Object ... paramValues) {
        String requestUrl = String.format(requestUrlPattern, paramValues);
        return handleGetSSL(requestUrl);
    }

    private static String handleGetSSL(String requestUrl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).build();

        HttpGet httpGet = new HttpGet(requestUrl);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            checkResponse(httpResponse);
            String resultStr = EntityUtils.toString(httpResponse.getEntity());
            EntityUtils.consume(httpResponse.getEntity());

            return resultStr;
        } catch (IOException e) {
            logger.error("Https请求IO异常！", e);
            throw new IllegalStateException("Https请求失败！");
        }
    }

    public static void writeBackSuccessToResponse(HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write("SUCCESS".getBytes());
        } catch (IOException e) {
            logger.error("回写SUCCESS IO异常", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    // 静默处理
                }
            }
        }
    }

    private static void checkResponse(HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            logger.error("Https请求失败！statusInfo={}", httpResponse.getStatusLine());
            throw new IllegalStateException("Https请求失败！");
        }
    }

    @SuppressWarnings("all")
    private static String appendRequestParams(String requestUrl, Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder(requestUrl + "?");
        for (String key : params.keySet()) {
            stringBuilder.append(key + "=" + params.get(key) + "&");
        }

        if (stringBuilder.charAt(stringBuilder.length() - 1) == '&') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            logger.error("SSLConnectionSocketFactory创建异常！", e);
            throw new IllegalStateException("构建Https请求失败");
        }
        return sslsf;
    }
}
