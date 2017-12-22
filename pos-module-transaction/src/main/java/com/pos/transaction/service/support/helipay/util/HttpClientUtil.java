/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合利宝HTTP请求工具类
 *
 * @author wangbing
 * @version 1.0, 2017/12/15
 */
public class HttpClientUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 以POST形式发起HTTP请求
     *
     * @param reqMap  请求数据
     * @param httpUrl 请求URL
     * @return HTTP响应数据
     * @throws ConnectTimeoutException http连接请求异常
     * @throws SocketTimeoutException  http响应超时异常
     * @throws IOException             响应读写异常
     */
    public static Map<String, Object> getHttpRes(Map<String, String> reqMap, String httpUrl)
            throws ConnectTimeoutException, SocketTimeoutException, IOException {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(httpUrl);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10 * 1000); // 等待超时 10s

        NameValuePair[] nvps = getNameValuePair(reqMap);
        method.setRequestBody(nvps);

        Map<String, Object> mp = new HashMap<>();
        try {
            int statusCode = client.executeMethod(method);
            LOG.info("请求响应状态statusCode:" + statusCode);
            mp.put("statusCode", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
                String curLine;
                StringBuilder buffer = new StringBuilder();
                while ((curLine = reader.readLine()) != null) {
                    buffer.append(curLine);
                }
                mp.put("response", buffer.toString());
            }
        } finally {
            method.releaseConnection(); // 释放连接
        }
        return mp;
    }

    private static NameValuePair[] getNameValuePair(Map<String, String> map) {
        List<NameValuePair> valueList = new ArrayList<>();
        map.forEach((key, value) -> valueList.add(new NameValuePair(key, value)));

        Object[] src = valueList.toArray();
        NameValuePair[] dest = new NameValuePair[src.length];
        System.arraycopy(src, 0, dest, 0, src.length);

        return dest;
    }
}
