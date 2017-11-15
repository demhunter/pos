/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.http;

import com.google.common.base.Strings;
import com.pos.common.util.basic.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpRequest工具类.
 *
 * @author wayne
 * @version 1.0, 2016/6/22
 */
public class HttpRequestUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    private HttpRequestUtils() {
    }

    /**
     * 获取客户端真实IP地址.
     * <p>
     * 如果服务器使用了反向代理, 则request.getRemoteAddr()无法获取客户端真实IP，该方法考虑了多级反向代理下获取客户端真实IP.
     *
     * @param request HttpServletRequest
     */
    public static String getRealRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (isUnknownIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnknownIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnknownIp(ip)) {
            ip = request.getRemoteAddr();
        }
        ip = ip.trim();

        // aliyun等云服务可能有多个代理, IP之间以逗号分隔, 第1个为客户端真实IP
        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取HTTP headers并转为JSON返回.
     *
     * @param request HttpServletRequest
     */
    public static String getHeaders2Json(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String next = headerNames.nextElement();
                headers.put(next, request.getHeader(next));
            }
        }

        return JsonUtils.objectToJson(headers);
    }

    /**
     * 获取HTTP parameters并转为JSON返回.
     *
     * @param request HttpServletRequest
     */
    public static String getParameters2Json(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String next = parameterNames.nextElement();
                parameters.put(next, request.getParameter(next));
            }
        }

        return JsonUtils.objectToJson(parameters);
    }

    /**
     * 获取HTTP session attributes并转为JSON返回.
     *
     * @param request HttpServletRequest
     */
    public static String getSessionAttributes2Json(HttpServletRequest request) {
        if (request == null || request.getSession() == null) {
            return null;
        }

        Map<String, Object> attributes = new HashMap<>();
        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        if (attributeNames != null) {
            while (attributeNames.hasMoreElements()) {
                String next = attributeNames.nextElement();
                attributes.put(next, request.getSession().getAttribute(next));
            }
        }

        return JsonUtils.objectToJson(attributes);
    }

    private static boolean isUnknownIp(String ip) {
        return Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }

    /**
     * 发起http请求
     *
     * @param requestUrl    请求路径
     * @param requestMethod 请求方法
     * @param outputStr     传输数据
     * @return 请求对应的响应
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            logger.error("连接超时：{}" + ce);
        } catch (Exception e) {
            logger.error("http请求异常：{}" + e);
        }
        return null;
    }

    /**
     * 发起http请求
     *
     * @param requestUrl    请求路径
     * @param requestMethod 请求方法
     * @param outputStr     传输数据
     * @return 请求对应的响应
     */
    public static String httpRequestIP(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=utf-8");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            logger.error("连接超时：{}" + ce);
        } catch (Exception e) {
            logger.error("http请求异常：{}" + e);
        }
        return null;
    }

    /**
     * 发起http请求
     *
     * @param requestUrl    请求路径
     * @param requestMethod 请求方法
     * @param headerMap     请求头数据
     * @param outputStr     传输数据
     * @return 请求对应的响应
     */
    public static String httpRequest(String requestUrl, String requestMethod, Map<String, String> headerMap, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            headerMap.keySet().forEach(key -> conn.setRequestProperty(key, headerMap.get(key)));
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            logger.error("连接超时：{}" + ce);
        } catch (Exception e) {
            logger.error("http请求异常：{}" + e);
        }
        return null;
    }
}