/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.common;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.pos.basic.manager.QiniuUploadManager;
import com.pos.common.util.mvc.support.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * POS 文件上传相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
@RestController
@RequestMapping("/upload")
@Api(value = "/upload", description = "v1.0.0 * 文件上传相关接口")
public class TokenManagerController {

    @Resource
    private QiniuUploadManager uploadManager;

    /**
     * 七牛图片传输获取token
     *
     * @return 发送结果
     */
    @RequestMapping(value = "getToken", method = RequestMethod.GET)
    @ApiOperation(value = "getToken", notes = "七牛图片传输获取token")
    public ApiResult<String> getUploadToken() {
        String token = uploadManager.getUpToken();
        ApiResult<String> apiResult = new ApiResult<>();
        if (token != null) {
            apiResult.setData(token);
        }
        return apiResult;
    }

}

