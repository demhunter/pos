/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support;

import com.pos.basic.constant.RedisConstants;
import com.pos.basic.service.MediaUploadService;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.dto.WechatAccessTokenDto;
import com.pos.transaction.dto.support.PosWeChatMediaDto;
import com.pos.transaction.exception.PosUserErrorCode;
import org.apache.commons.lang3.StringUtils;
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
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 微信素材Support
 *
 * @author wangbing
 * @version 1.0, 2017/10/26
 */
@Component
@SuppressWarnings("all")
public class PosWeChatMediaSupport {

    private final static Logger logger = LoggerFactory.getLogger(PosWeChatMediaSupport.class);

    // 微信临时素材请求地址
    private final static String WECHAT_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/get";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PosConstants posConstants;

    @Resource
    private MediaUploadService mediaUploadService;

    public ApiResult<String> getWeChatMedia(String mediaId) {
        FieldChecker.checkEmpty(mediaId, "mediaId");

        String accessToken = getWeChatToken();
        if (StringUtils.isEmpty(accessToken)) {
            return ApiResult.fail(PosUserErrorCode.WECHAT_ERROR_FOR_ACCESS_TOKEN);
        }

        String mediaUrl = WECHAT_MEDIA_URL + "?access_token=" + accessToken + "&media_id=" + mediaId;

        PosWeChatMediaDto media = doHttpGet(mediaUrl);
        if (!media.isSuuc()) {
            logger.error("获取微信素材mediaId={}失败！token={}，errcode={}，errmsg={}", mediaId, media.getErrcode(), media.getErrmsg());
            return ApiResult.fail(PosUserErrorCode.WECHAT_ERROR_UPLOAD_IMAGE);
        }

        return mediaUploadService.uploadMediaStreamToQiNiu(media.getData());
    }

    private String getWeChatToken() {
        String accessToken = redisTemplate.opsForValue().get(RedisConstants.WECHAT_ACCESS_TOKEN);//在redis里面缓存的access_token

        if (StringUtils.isBlank(accessToken)) {
            String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + posConstants.getPosWeChatAppId() + "&secret=" + posConstants.getPosWeChatSecret();
            String accessTokenResultStr = HttpRequestUtils.httpRequest(accessTokenUrl, "GET", null);
            if (StringUtils.isNotBlank(accessTokenResultStr)) {
                WechatAccessTokenDto wechatAccessTokenDto = JsonUtils.jsonToObject(accessTokenResultStr, new TypeReference<WechatAccessTokenDto>() {
                });
                if (wechatAccessTokenDto.accessTokenRequestSucc()) {
                    accessToken = wechatAccessTokenDto.getAccess_token();
                    redisTemplate.opsForValue().set(RedisConstants.WECHAT_ACCESS_TOKEN, wechatAccessTokenDto.getAccess_token(), wechatAccessTokenDto.getExpires_in(), TimeUnit.SECONDS);
                } else {
                    logger.error("获取微信access_token失败。errcode={},errmsg={}", wechatAccessTokenDto.getErrcode(), wechatAccessTokenDto.getErrmsg());
                }
            }
        }
        logger.info("wechat token:" + accessToken);

        return accessToken;
    }

    /**
     * 发起请求，获取素材信息
     *
     * @param requestUrl 请求路径
     * @return 请求对应的响应
     */
    private byte[] doGetMedia(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod("GET");
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
            return buffer.toString().getBytes();
        } catch (ConnectException ce) {
            logger.error("连接超时：{}" + ce);
        } catch (Exception e) {
            logger.error("http请求异常：{}" + e);
        }
        return null;
    }

    private PosWeChatMediaDto doHttpGet(String requestUrl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).build();

        HttpGet httpGet = new HttpGet(requestUrl);
        try {
            PosWeChatMediaDto result = new PosWeChatMediaDto();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            checkResponse(httpResponse);
            String contentType = httpResponse.getHeaders("Content-Type")[0].getValue();
            if ("image/jpeg".equals(contentType)) {
                result.setData(EntityUtils.toByteArray(httpResponse.getEntity()));
            } else {
                String resultStr = EntityUtils.toString(httpResponse.getEntity());
                result = JsonUtils.jsonToObject(resultStr, new TypeReference<PosWeChatMediaDto>() {});
            }
            EntityUtils.consume(httpResponse.getEntity());

            return result;
        } catch (IOException e) {
            logger.error("Https请求IO异常！", e);
            throw new IllegalStateException("Https请求失败！");
        }
    }

    private static void checkResponse(HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            logger.error("Https请求失败！statusInfo={}", httpResponse.getStatusLine());
            throw new IllegalStateException("Https请求失败！");
        }
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
