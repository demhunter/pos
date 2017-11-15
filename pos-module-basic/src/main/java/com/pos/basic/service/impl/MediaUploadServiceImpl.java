/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.google.gson.Gson;
import com.pos.basic.exception.BasicErrorCode;
import com.pos.basic.manager.QiniuUploadManager;
import com.pos.basic.service.MediaUploadService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * 多媒体上传ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/10/26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaUploadServiceImpl implements MediaUploadService {

    private final static Logger logger = LoggerFactory.getLogger(MediaUploadServiceImpl.class);

    @Resource
    private QiniuUploadManager qiniuUploadManager;

    @Value("${qiniu.bucket.image.address}")
    private String qiniuBucketImageAddress; // 七牛图片空间地址

    @Override
    public ApiResult<String> uploadMediaStreamToQiNiu(byte[] data) {
        FieldChecker.checkEmpty(data, "mediaStream");
        UploadManager uploadManager = new UploadManager();

        try {
            BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(data));
            int width = sourceImg.getWidth();
            int height = sourceImg.getHeight();
            logger.info("width={}, height={}", width, height);
            Response response = uploadManager.put(data, null, qiniuUploadManager.getUpToken());
            if (response.isOK()) {
                logger.info("response={}", JsonUtils.objectToJson(response));
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                logger.info("key={}, hash={}", putRet.key, putRet.hash);

                return ApiResult.succ(qiniuBucketImageAddress + putRet.hash + "?width=" + width + "&height=" + height);
            }
            return ApiResult.fail(BasicErrorCode.QINIU_UPLOAD_IMAGE_ERROR);
        } catch (QiniuException qiniuException) {
            logger.error("上传七牛失败：exception={}", qiniuException);
            return ApiResult.fail(BasicErrorCode.QINIU_UPLOAD_IMAGE_ERROR);
        } catch (Exception e) {
            logger.error("上传七牛失败：exception={}", e);
            return ApiResult.fail(BasicErrorCode.QINIU_UPLOAD_ERROR);
        }
    }
}
