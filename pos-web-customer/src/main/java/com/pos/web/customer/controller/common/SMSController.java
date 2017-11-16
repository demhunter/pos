/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.common;

import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.mvc.support.ApiResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
@RestController
@RequestMapping("/sms")
@Api(value = "/sms", description = "v1.0.0 * 短信相关接口")
public class SMSController {

    @Resource
    private SmsService smsService;

    @RequestMapping(value = "verify-code", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 发送短信验证码请求", notes = "发送短信验证码请求(v1.0.0 * 请求地址发生变化)")
    public ApiResult sendSmsCode(
            @ApiParam(name = "phone", value = "用户手机号")
            @RequestParam("phone") String phone,
            @ApiParam(name = "source", value = "请求来源：1 = 用户注册，2 = 找回密码，3 = 登录验证")
            @RequestParam("source") int source) {
        ApiResult apiResult = smsService.sendVerifyCode(phone, MemcachedPrefixType.getEnum((byte) source));
        if (!apiResult.isSucc()) {
            apiResult.setMessage("验证码发送失败");
        }

        return apiResult;
    }
}
