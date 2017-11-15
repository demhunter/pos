/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.sms.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * 构建短信消息
 *
 * Created by cc on 16/5/27.
 */
public class SmsMessageUtil {

    private static final char[] NUMERICS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 格式化短信
     *
     * @param smsTemplate 短信模板
     * @param vars 短信变量列表，可以空，如果vars长度小于模板变量数则会抛出MissingFormatArgumentException异常
     * @return 格式化后的短信
     *
     * @throws java.util.MissingFormatArgumentException
     */
    public static String formatSms(String smsTemplate, Object... vars) {
        String formattedSms = StringUtils.EMPTY;

        if (StringUtils.isEmpty(smsTemplate)) {
            return formattedSms;
        }

        return String.format(smsTemplate, vars);
    }

    /**
     * 产生随机验证码
     *
     * @param codeLength 验证码长度
     * @return 验证码字符串
     */
    public static String generateVerifyCode(int codeLength) {
        Random random = new Random();
        StringBuilder verifyCodeBuilder = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(10);
            verifyCodeBuilder.append(NUMERICS[index]);
        }

        return verifyCodeBuilder.toString();
    }
}
