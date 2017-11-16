/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.common;

import com.google.common.base.Strings;
import com.pos.basic.manager.QiniuUploadManager;
import com.pos.common.util.exception.CommonErrorCode;
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
 * 文件上传相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/16
 */
@RestController
@RequestMapping("/upload/")
@Api(value = "/upload", description = "v1.0.0 * 文件上传相关接口")
public class UploadManagerController {

    @Resource
    private QiniuUploadManager uploadManager;

    @RequestMapping(value = "qiniu/video/persist", method = RequestMethod.GET)
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
