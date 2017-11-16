/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.common;

import com.pos.web.customer.vo.token.QiNiuTokenVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.pos.basic.manager.QiniuUploadManager;
import com.pos.common.util.mvc.support.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * token管理相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
@RestController
@RequestMapping("/token")
@Api(value = "/token", description = "v1.0.0 * token管理相关接口")
public class TokenManagerController {

    @Resource
    private QiniuUploadManager uploadManager;

    @RequestMapping(value = "qiniu/image", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取七牛图片传输token", notes = "获取七牛图片传输token")
    public ApiResult<String> getUploadToken() {
        String token = uploadManager.getUpToken();
        ApiResult<String> apiResult = new ApiResult<>();
        if (token != null) {
            apiResult.setData(token);
        }
        return apiResult;
    }

    @RequestMapping(value = "qiniu/image/uptoken", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取七牛图片传输uptoken，用户前端JS SDK的uptoken_url", notes = "获取七牛图片传输uptoken，用户前端JS SDK的uptoken_url")
    public QiNiuTokenVo getQiNiuToken() {
        QiNiuTokenVo tokenVo = new QiNiuTokenVo();
        tokenVo.setUptoken(uploadManager.getUpToken());
        return tokenVo;
    }

}

