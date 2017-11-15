/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.common;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.pos.service.support.PosWeChatMediaSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 图片相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/26
 */
@RestController
@RequestMapping("/images")
@Api(value = "/images", description = "图片接口")
public class PosImagesController {

    private final static Logger logger = LoggerFactory.getLogger(PosImagesController.class);

    @Resource
    private PosWeChatMediaSupport posWeChatMediaSupport;

    @RequestMapping(value = "upload/wechat-media", method = RequestMethod.GET)
    @ApiOperation(value = "上传微信的多媒体素材到七牛", notes = "上传微信的多媒体素材到七牛")
    public ApiResult<String> uploadWeChatImageToQiNiu(
            @ApiParam(name = "mediaId", value = "微信多媒体素材id（图片格式的素材）")
            @RequestParam("mediaId") String mediaId) {
        logger.info("微信素材mediaId={}", mediaId);
        return posWeChatMediaSupport.getWeChatMedia(mediaId);
    }
}
