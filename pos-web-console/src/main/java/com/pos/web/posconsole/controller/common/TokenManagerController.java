/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.controller.common;

import com.google.common.base.Strings;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.pos.basic.manager.QiniuUploadManager;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * POS 文件上传相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
@RestController
@RequestMapping("/upload/")
@Api(value = "/upload", description = "文件上传相关接口")
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

    /**
     * 七牛视频传输获取token
     *
     * @return 发送结果
     */
    @RequestMapping(value = "getVideoToken", method = RequestMethod.GET)
    @ApiOperation(value = "七牛视频传输获取token", notes = "七牛视频传输获取token")
    public ApiResult<String> getVideoUploadToken() {
        String token = uploadManager.getVideoUpToken();
        ApiResult<String> apiResult = new ApiResult<>();
        if (token != null) {
            apiResult.setData(token);
        }
        return apiResult;
    }

    @RequestMapping(value = "persistVideo", method = RequestMethod.GET)
    @ApiOperation(value = "持久化并转码已上传到七牛的视频", notes = "持久化并转码已上传到七牛的视频")
    public ApiResult<String> persistVideo(
            @ApiParam(name = "fileKey", value = "要持久化的文件KEY")
            @RequestParam("fileKey") String fileKey) {
        String persistId = uploadManager.persistVideo(fileKey);
        if (!Strings.isNullOrEmpty(persistId)) {
            return ApiResult.succ(persistId);
        } else {
            return ApiResult.fail(CommonErrorCode.FILE_UPLOAD_FAILED);
        }
    }

}

