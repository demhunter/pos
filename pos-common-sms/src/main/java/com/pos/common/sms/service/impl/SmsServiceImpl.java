/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.sms.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.domain.Sms;
import com.pos.common.sms.service.SmsService;
import com.pos.common.sms.dao.SmsDao;
import com.pos.common.sms.util.SmsMessageUtil;
import com.pos.common.util.cache.MemcachedClientUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.Preconditions;
import com.pos.common.util.validation.Validator;
import net.rubyeye.xmemcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 短信下行服务
 *
 * Created by cc on 16/6/8.
 */
@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    /**
     * 短信网关帐号
     */
    @Value("${sms.account}")
    private String account;

    /**
     * 短信网关密码
     */
    @Value("${sms.passwd}")
    private String passwd;

    /**
     * 短信网关主机
     */
    @Value("${sms.send.host}")
    private String sendHost;

    /**
     * 短信发送接口路径
     */
    @Value("${sms.send.path}")
    private String sendPath;

    /**
     * 短信验证码长度
     */
    @Value("${sms.verify.code.length}")
    private String verifyCodeLength;

    /**
     * Http请求超时时间
     */
    @Value("${sms.http.timeout}")
    private String httpTimeout;

    /**
     * 缓存中验证码过期时间
     */
    @Value("${cache.expire.sms.verify.code}")
    private String cacheExpireSeconds;

    /**
     * 短信下发总开关
     */
    @Value("${sms.send.switch}")
    private String smsSendSwitch;

    /**
     * 缓存工具
     */
    @Resource
    private MemcachedClientUtils memcached;

    /**
     * SmsDao
     */
    @Resource
    private SmsDao smsDao;

    /**
     * 缓存客户端
     */
    private MemcachedClient memcachedClient;

    /**
     * 配置属性
     */
    private Properties properties;

    /**
     * 读取配置文件并提取缓存客户端
     */
    @PostConstruct
    public void afterConstruct() {
        properties = new Properties();

        try {
            properties.load(new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("sms-template.properties"), "UTF-8"));
        } catch (IOException e) {
            logger.error("[SMS]读取配置失败");
            throw new RuntimeException("载入模板文件失败");
        }

        memcachedClient = memcached.getMemcachedClient();
    }

    /**
     * 检查验证码的合法性
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param inputCode 用户输入的验证码
     * @param codeType 验证码使用类型
     * @return 检查结果
     */
    @Override
    public ApiResult checkVerifyCode(String phoneNumber, String inputCode, MemcachedPrefixType codeType) {
        String verifyCode = StringUtils.EMPTY;
        String cacheKey = StringUtils.EMPTY;

        try {
            if (codeType.equals(MemcachedPrefixType.REGISTER)) {
                cacheKey = MemcachedPrefixType.REGISTER.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            } else if (codeType.equals(MemcachedPrefixType.RETRIEVE)) {
                cacheKey = MemcachedPrefixType.RETRIEVE.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            } else if (codeType.equals(MemcachedPrefixType.LOGIN)) {
                cacheKey = MemcachedPrefixType.LOGIN.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            } else if (codeType.equals(MemcachedPrefixType.COMPANY_APPLY)) {
                cacheKey = MemcachedPrefixType.COMPANY_APPLY.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            } else if (codeType.equals(MemcachedPrefixType.TWITTER_CUSTOMER_REPORT)) {
                cacheKey = MemcachedPrefixType.TWITTER_CUSTOMER_REPORT.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            } else if (codeType.equals(MemcachedPrefixType.TWITTER_INVITATION)) {
                cacheKey = MemcachedPrefixType.TWITTER_INVITATION.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            }else if (codeType.equals(MemcachedPrefixType.MATERIAL_JOIN)) {
                cacheKey = MemcachedPrefixType.MATERIAL_JOIN.getPrefix() + phoneNumber;
                verifyCode = memcachedClient.get(cacheKey);
            }

            if (StringUtils.isNotEmpty(verifyCode) && verifyCode.equals(inputCode)) {
                memcachedClient.delete(cacheKey);
                return ApiResult.succ();
            }
        } catch (Exception e) {
            logger.error("[SMS]查询Cache失败，phoneNumber={}，inputCode={}，codeType={}",
                    phoneNumber, inputCode, codeType.getDesc(), e);
        }

        return ApiResult.fail(CommonErrorCode.VERIFY_CODE_ERROR);
    }

    /**
     * 发送短信
     *
     * @param phoneNumber 手机号码
     * @param message 短信内容
     * @return 发送结果
     */
    private boolean sendSms(String phoneNumber, String message) {
        // 1. 格式校验
        String strippedPhoneNumber = stripPhoneNumber(phoneNumber);
        try {
            Validator.checkMobileNumber(strippedPhoneNumber);
        } catch (ValidationException e) {
            logger.warn("[SMS]%s，phoneNumber＝{} message={}", e.getMessage(), phoneNumber, message);
            return false;
        }

        if (smsSendSwitch.equals(Boolean.TRUE.toString())) {
            // 2. 构建http get请求
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URI requestUri = buildRequestUri(phoneNumber, message);
            if (requestUri == null) {
                return false;
            }
            HttpGet httpGet = new HttpGet(requestUri);

            // 3. 设置http请求超时，ConnectTimeout为连接超时时间，SocketTimeout为获取数据超时时间
            RequestConfig requestConfig = RequestConfig.custom().
                    setConnectTimeout(Integer.valueOf(httpTimeout)).
                    setSocketTimeout(Integer.valueOf(httpTimeout)).build();
            httpGet.setConfig(requestConfig);

            // 4. 执行请求，提取结果
            return executeAndExtractResult(httpClient, httpGet, phoneNumber);
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param codeType 验证码使用类型
     * @return 发送结果
     */
    @Override
    public ApiResult sendVerifyCode(String phoneNumber, MemcachedPrefixType codeType) {
        String verifyCode = SmsMessageUtil.generateVerifyCode(Integer.valueOf(verifyCodeLength));
        String message = formatVerifyCodeMessage(verifyCode, codeType);
        boolean isSuccess = sendSms(phoneNumber, message);

        ApiResult apiResult;
        // 添加或更新缓存
        if (isSuccess) {
            boolean setResult = setCache(phoneNumber, codeType, verifyCode);
            if (!setResult) {
                apiResult = ApiResult.fail(CommonErrorCode.CACHE_OPERATE_ERROR);
            } else {
                apiResult = ApiResult.succ();
            }
        } else {
            apiResult = ApiResult.fail(CommonErrorCode.VERIFY_CODE_ERROR);
        }

        // 记录日志并返回结果
        Sms sms = new Sms();
        sms.setPhone(phoneNumber);
        sms.setContent(message);
        sms.setType(codeType.getCode());
        sms.setStatus((byte) apiResult.getStateCode());
        sms.setSendTime(new Date());
        if (isSuccess) {
            logger.info("短信信息：{}", sms);
            smsDao.saveSmsInfo(sms);
        } else {
            logger.error("短信发送失败，短信信息：{}", sms);
        }

        return apiResult;
    }

    /**
     * 发送格式化好的短信
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param message 短信内容
     * @return 发送结果
     */
    @Override
    public ApiResult sendMessage(String phoneNumber, String message) {
        boolean isSuccess = sendSms(phoneNumber, message);

        ApiResult apiResult;
        if (isSuccess) {
            apiResult = ApiResult.succ();
        } else {
            apiResult = ApiResult.fail(CommonErrorCode.SMS_SEND_FAILED);
        }

        // 记录日志并返回结果
        Sms sms = new Sms();
        sms.setPhone(phoneNumber);
        sms.setContent(message);
        sms.setType(MemcachedPrefixType.EMPLOYEE.getCode());
        sms.setStatus((byte) apiResult.getStateCode());
        sms.setSendTime(new Date());
        if (isSuccess) {
            logger.info("短信信息：{}", sms);
            smsDao.saveSmsInfo(sms);
        } else {
            logger.error("短信发送失败，短信信息：{}", sms);
        }

        return apiResult;
    }

    /**
     * 批量发送短信
     *
     * @param phoneNumbers 电话号码列表
     * @param messages 消息列表，与电话号码列表的下标一一对应
     * @return 发送失败的电话号码列表
     */
    public ApiResult<List<String>> sendMessageBatch(List<String> phoneNumbers, List<String> messages) {
        Preconditions.checkArgument(
                        phoneNumbers != null && messages != null &&
                        phoneNumbers.size() == messages.size(), "批量短信发送参数不合法！");

        List<String> failPhoneList = Lists.newArrayList();

        int index = 0;
        for (String phoneNumber : phoneNumbers) {
            ApiResult result = sendMessage(phoneNumber, messages.get(index++));
            if (!result.isSucc()) {
                failPhoneList.add(phoneNumber);
            }
        }

        if (!CollectionUtils.isEmpty(failPhoneList)) {
            ApiResult<List<String>> failResult = ApiResult.fail(CommonErrorCode.SMS_SEND_FAILED);
            failResult.setData(failPhoneList);

            return failResult;
        }

        return ApiResult.succ();
    }

    /**
     * 批量发送短信
     *
     * @param phoneNumbers 电话号码列表
     * @param message 发送消息
     * @return 发送失败的电话号码列表
     */
    @Override
    public ApiResult<List<String>> sendMessageBatch(List<String> phoneNumbers, String message) {
        Preconditions.checkNotNull(phoneNumbers, "批量短信发送参数不合法！");

        List<String> messages = Lists.newArrayListWithExpectedSize(phoneNumbers.size());
        for (int i = 0; i < phoneNumbers.size(); i++) {
            messages.add(message);
        }

        return sendMessageBatch(phoneNumbers, messages);
    }

    /**
     * 执行请求并处理结果
     *
     * @param httpClient CloseableHttpClient
     * @param httpGet HttpGet
     * @param phoneNumber 手机号码
     * @return 处理结果
     */
    private boolean executeAndExtractResult(
            CloseableHttpClient httpClient, HttpGet httpGet, String phoneNumber) {
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity;
        boolean sendResult;
        try {
            httpResponse = httpClient.execute(httpGet);
            entity = httpResponse.getEntity();
            if (entity == null) {
                logger.error("[SMS]HttpEntity为空");
                return false;
            }
            String resultStr = EntityUtils.toString(entity);
            sendResult = handleResult(resultStr, phoneNumber);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            logger.error("[SMS]执行http请求过程中发送IO异常，phoneNumber={}",
                    phoneNumber, e);
            return false;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // 静默处理
                }
            }
        }

        return sendResult;
    }

    /**
     * 设置缓存
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param codeType 验证码使用类型
     * @param verifyCode 验证码
     * @return 设置结果
     */
    private boolean setCache(String phoneNumber, MemcachedPrefixType codeType, String verifyCode) {
        boolean setResult;

        try {
            if (codeType.equals(MemcachedPrefixType.REGISTER)) {
                setResult = memcachedClient.set(
                        MemcachedPrefixType.REGISTER.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            } else if (codeType.equals(MemcachedPrefixType.RETRIEVE)) {
                setResult = memcachedClient.set(
                        MemcachedPrefixType.RETRIEVE.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            } else if (codeType.equals(MemcachedPrefixType.LOGIN)) {
                setResult = memcachedClient.set(
                        MemcachedPrefixType.LOGIN.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            } else if (codeType.equals(MemcachedPrefixType.COMPANY_APPLY)) {
                setResult = memcachedClient.set(
                        MemcachedPrefixType.COMPANY_APPLY.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            } else if (codeType.equals(MemcachedPrefixType.TWITTER_CUSTOMER_REPORT)) {
                setResult = memcachedClient.set(
                        MemcachedPrefixType.TWITTER_CUSTOMER_REPORT.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            } else if (codeType.equals(MemcachedPrefixType.TWITTER_INVITATION)) {
                setResult = memcachedClient.set(
                        MemcachedPrefixType.TWITTER_INVITATION.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            }else if(codeType.equals(MemcachedPrefixType.MATERIAL_JOIN)){
                setResult = memcachedClient.set(
                        MemcachedPrefixType.MATERIAL_JOIN.getPrefix() + phoneNumber, Integer.valueOf(cacheExpireSeconds), verifyCode);
            } else {
                throw new UnsupportedOperationException("暂不支持的添加缓存操作");
            }
        } catch (Exception e) {
            logger.error("[SMS]验证码存入缓存失败，phoneNumber={}，verifyCode={}", phoneNumber, verifyCode, e);
            return false;
        }

        return setResult;
    }

    /**
     * 处理请求结果
     *
     * @param resultStr 请求结果字符串
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @return 处理结果
     */
    private boolean handleResult(String resultStr, String phoneNumber) {
        Iterable<String> lines = Splitter.on('\n').omitEmptyStrings().trimResults().split(resultStr);
        Iterator<String> iter = lines.iterator();
        if (iter.hasNext()) {
            String timeAndStatus = iter.next();
            int commaIndex = timeAndStatus.lastIndexOf(',');
            String time = timeAndStatus.substring(0, commaIndex);
            String status = timeAndStatus.substring(commaIndex + 1);

            if (Integer.valueOf(status) != 0) {
                logger.warn("[SMS]短信发送失败，time={}，status={}，phoneNumber={}",
                        time, status, phoneNumber);
                return false;
            } else {
                logger.info("[SMS]短信发送成功，time={}，phoneNumber={}",
                        time, phoneNumber);
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 格式化验证码模板
     *
     * @param verifyCode 验证码
     * @param codeType 验证码使用类型
     * @return 格式化结果
     */
    private String formatVerifyCodeMessage(String verifyCode, MemcachedPrefixType codeType) {
        if (codeType.equals(MemcachedPrefixType.REGISTER)) {
            return SmsMessageUtil.formatSms(properties.getProperty("register.verify.code.template"), verifyCode);
        } else if (codeType.equals(MemcachedPrefixType.RETRIEVE)) {
            return SmsMessageUtil.formatSms(properties.getProperty("retrieve.verify.code.template"), verifyCode);
        } else if (codeType.equals(MemcachedPrefixType.COMPANY_APPLY)) {
            return SmsMessageUtil.formatSms(properties.getProperty("business.apply.template"), verifyCode);
        } else if (codeType.equals(MemcachedPrefixType.TWITTER_CUSTOMER_REPORT)) {
            return SmsMessageUtil.formatSms(properties.getProperty("customer.twitter.report.template"), verifyCode);
        } else if (codeType.equals(MemcachedPrefixType.TWITTER_INVITATION)) {
            return SmsMessageUtil.formatSms(properties.getProperty("twitter.invitation.template"), verifyCode);
        } else if(codeType.equals(MemcachedPrefixType.MATERIAL_JOIN)){
            return SmsMessageUtil.formatSms(properties.getProperty("material.invitation.template"), verifyCode);
        }else{
            throw new UnsupportedOperationException("暂不支持的格式化操作");
        }
    }

    /**
     * 构建Http请求URI
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param message 短信内容
     * @return 构建好的URI
     */
    private URI buildRequestUri(String phoneNumber, String message) {
        URI uri = null;

        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(sendHost)
                    .setPath(sendPath)
                    .setParameter("account", account)
                    .setParameter("pswd", passwd)
                    .setParameter("mobile", phoneNumber)
                    .setParameter("msg", message)
                    .setParameter("needstatus", String.valueOf(Boolean.TRUE))
                    .setParameter("extno", null)
                    .build();
        } catch (URISyntaxException e) {
            logger.error("[SMS]构建URI失败");
        }

        return uri;
    }

    /**
     * 修整电话号码
     *
     * @param phoneNumber 初始电话号码
     * @return 休整后的结果
     */
    private String stripPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return StringUtils.EMPTY;
        }

        String strippedPhoneNumber = StringUtils.EMPTY;
        if (phoneNumber.startsWith("+86")) {
            if (phoneNumber.length() == 14) {// +8613412345678
                strippedPhoneNumber =  phoneNumber.substring(3);
            } else if (phoneNumber.length() == 15) {// +86 13412345678
                strippedPhoneNumber = phoneNumber.substring(4);
            }
        } else {
            return phoneNumber;
        }

        return strippedPhoneNumber;
    }
}
