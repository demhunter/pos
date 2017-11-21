/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.manager;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.validation.Preconditions;
import com.pos.common.util.web.http.HttpClientUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Formatter;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微信公众平台Token Manager
 *
 * @author cc
 * @version 1.0, 16/9/14
 */
public class WeixPlatformManager {

    private static final Logger logger = LoggerFactory.getLogger(WeixPlatformManager.class);

    @Value("${weix.app.id}")
    private String appId;

    @Value("${weix.app.secret}")
    private String appSecret;

    @Value("${weix.token.url}")
    private String tokenUrl;

    @Value("${weix.ticket.url}")
    private String ticketUrl;

    @Value("${weix.token.duration}")
    private String tokenDuration;

    @Value("${weix.ticket.duration}")
    private String ticketDuration;

    private String token;

    private Date tokenCreateTime;

    private String ticket;

    private Date ticketCreateTime;

    private ReentrantLock tokenReentrantLock = new ReentrantLock();

    private ReentrantLock ticketReentrantLock = new ReentrantLock();

    public String getAppId() {
        return appId;
    }

    public void updateTicket() {
        if (tokenCreateTime == null || SimpleDateUtils.secondsOfDuration(tokenCreateTime, new Date()) > Integer.valueOf(tokenDuration)) {
            tokenReentrantLock.lock();

            String resultJson = HttpClientUtils.doGetSSL(tokenUrl, appId, appSecret);
            Map<String, String> resultMap = JsonUtils.jsonToObject(resultJson, new TypeReference<Map<String, String>>() {});
            Preconditions.checkNotNull(resultMap, "请求Token结果不能为空！");
            token = resultMap.get("access_token");
            tokenCreateTime = new Date();

            tokenReentrantLock.unlock();
        }

        if (ticketCreateTime == null || SimpleDateUtils.secondsOfDuration(ticketCreateTime, new Date()) > Integer.valueOf(tokenDuration)) {
            ticketReentrantLock.lock();

            String resultJson = HttpClientUtils.doGetSSL(ticketUrl, token);
            Map<String, String> resultMap = JsonUtils.jsonToObject(resultJson, new TypeReference<Map<String, String>>() {});
            Preconditions.checkNotNull(resultMap, "请求Ticket结果不能为空！");
            ticket = resultMap.get("ticket");
            ticketCreateTime = new Date();

            ticketReentrantLock.unlock();
        }
    }

    public String acquireSignature(String timestamp, String nonce, String requestUrl) {
        String originStr =
                "jsapi_ticket=" + ticket + "&noncestr=" + nonce + "&timestamp=" + timestamp + "&url=" + requestUrl;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(originStr.getBytes("UTF-8"));
            return byteToHex(crypt.digest());
        }
        catch (Exception e) {
            logger.error("SHA-1签名失败！", e);
            throw new IllegalStateException("SHA-1签名失败！");
        }
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
