/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.sms.service;

import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.util.mvc.support.ApiResult;

import java.util.List;

/**
 * 短信下行服务接口
 * <p>
 * Created by cc on 16/6/8.
 */
public interface SmsService {

    /**
     * 发送验证码
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param codeType    验证码使用类型
     * @return 发送结果
     */
    ApiResult sendVerifyCode(String phoneNumber, MemcachedPrefixType codeType);

    /**
     * 发送短信
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param message     短信内容
     * @return 发送结果
     */
    ApiResult sendMessage(String phoneNumber, String message);

    /**
     * 批量发送短信
     *
     * @param phoneNumbers 电话号码列表
     * @param messages     消息列表，与电话号码列表的下标一一对应
     * @return 发送失败的电话号码列表
     */
    ApiResult<List<String>> sendMessageBatch(List<String> phoneNumbers, List<String> messages);

    /**
     * 批量发送短信
     *
     * @param phoneNumbers 电话号码列表
     * @param message      发送消息
     * @return 发送失败的电话号码列表
     */
    ApiResult<List<String>> sendMessageBatch(List<String> phoneNumbers, String message);

    /**
     * 校验验证码
     *
     * @param phoneNumber 电话号码，仅支持13412345678，+86 13412345678，+8613412345678三种合法形式
     * @param inputCode   用户输入的验证码
     * @param codeType    验证码使用类型
     * @return 验证结果
     */
    ApiResult checkVerifyCode(String phoneNumber, String inputCode, MemcachedPrefixType codeType);
}
