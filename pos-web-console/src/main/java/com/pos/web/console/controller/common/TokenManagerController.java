/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.common;

import com.pos.basic.manager.QiniuUploadManager;
import com.pos.common.util.mvc.support.ApiResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * POS token管理相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
@RestController
@RequestMapping("/token")
@Api(value = "/token", description = "v1.0.0 * token管理相关接口(主要为七牛上传Token)")
public class TokenManagerController {

    @Resource
    private QiniuUploadManager uploadManager;

    @RequestMapping(value = "qiniu/image", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 七牛图片传输获取token", notes = "七牛图片传输获取token")
    public ApiResult<String> getUploadToken() {
        String token = uploadManager.getUpToken();
        ApiResult<String> apiResult = new ApiResult<>();
        if (token != null) {
            apiResult.setData(token);
        }
        return apiResult;
    }

    @RequestMapping(value = "qiniu/video", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 七牛视频传输获取token", notes = "七牛视频传输获取token")
    public ApiResult<String> getVideoUploadToken() {
        String token = uploadManager.getVideoUpToken();
        ApiResult<String> apiResult = new ApiResult<>();
        if (token != null) {
            apiResult.setData(token);
        }
        return apiResult;
    }

}

