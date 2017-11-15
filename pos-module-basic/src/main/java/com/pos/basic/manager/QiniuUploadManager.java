/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.manager;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 七牛上传Manager
 *
 * Created by WangShengzhi on 2016/6/13.
 */
@Component
public class QiniuUploadManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 设置好账号的ACCESS_KEY和SECRET_KEY
    @Value("${qiniu.access.key}")
    private String accessKey;

    @Value("${qiniu.secret.key}")
    private String secretKey;

    // 要上传的图片空间
    @Value("${qiniu.bucket.image.name}")
    private String bucketImageName;

    // 要上传的视频空间
    @Value("${qiniu.bucket.video.name}")
    private String bucketVideoName;

    // 全局token
    private static String imageToken = null;
    private static String videoToken = null;
    private static long imageTokenExpiredTime = 0L;
    private static long videoTokenExpiredTime = 0L;
    private static final long EXPIRE_DURATION = (3000L * 1000L);

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public synchronized String getUpToken() {
        if (imageToken == null || imageToken.isEmpty() || isImageTokenExpired()) {
            //密钥配置
            Auth auth = Auth.create(accessKey, secretKey);
            //创建上传对象
            imageToken = auth.uploadToken(bucketImageName);
            setImageTokenExpiredTime();
        }
        return imageToken;
    }

    // 简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public synchronized String getVideoUpToken() {
        if (videoToken == null || videoToken.isEmpty() || isVideoTokenExpired()) {
            //密钥配置
            Auth auth = Auth.create(accessKey, secretKey);
            //创建上传对象
            videoToken = auth.uploadToken(bucketVideoName);
            setVideoTokenExpiredTime();
        }
        return videoToken;
    }

    public String persistVideo(String fileKey) {
        //设置账号的AK,SK
        Auth auth = Auth.create(accessKey, secretKey);
        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth);
        //设置转码操作参数
        String fops = "avthumb/mp4/s/640x360/vb/1.25m";
        //设置转码的队列
        String pipeline = "ywmj-video-pipeline";
        String urlbase64 = UrlSafeBase64.encodeToString(bucketVideoName + ":" + "o_1ash2ot4l1ek0uqhqtp1hcr189uh.mp4");
        String pfops = fops + "|saveas/"+urlbase64;
        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        try {
            String persistId = operater.pfop(bucketVideoName, fileKey, fops, params);
            return persistId;
        } catch (QiniuException e) {
            logger.error("持久化七牛视频文件失败！cause: " + e.response.toString());
            return null;
        }
    }

    public static void main(String[] args) {
        String accessKey = "kaUCkQ9734hwU0O4G2qbhYBVIJ3u7yDuNDLiXNKs";
        String secretKey = "XbxrwLJPpaJ2BCnUOz4rUTYu_eGNga3t9xuyHwbG";
        String bucketVideoName = "ywmjvideo";
        //设置账号的AK,SK
        Auth auth = Auth.create(accessKey, secretKey);
        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth);
        //设置转码操作参数
        String fops = "avthumb/mp4/s/640x360/vb/1.25m";
        //设置转码的队列
        String pipeline = "ywmj-video-pipeline";
        //可以对转码后的文件使用saveas参数自定义命名，当然也可以不指定，文件会默认命名并保存在当前空间
        String urlbase64 = UrlSafeBase64.encodeToString(bucketVideoName + ":" + "o_1ash2ot4l1ek0uqhqtp1hcr189uh.mp4");
        String pfops = fops + "|saveas/"+urlbase64;
        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline);
        try {
            String persistid = operater.pfop(bucketVideoName, "o_1ash67f6jso1brg4vr1h431m69h.mov", fops, params);
            //打印返回的persistid
            System.out.println(persistid);
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }

    private void setImageTokenExpiredTime() {
        imageTokenExpiredTime = System.currentTimeMillis() + EXPIRE_DURATION;
    }

    private boolean isImageTokenExpired() {
        return System.currentTimeMillis() > imageTokenExpiredTime;
    }

    private void setVideoTokenExpiredTime() {
        videoTokenExpiredTime = System.currentTimeMillis() + EXPIRE_DURATION;
    }

    private boolean isVideoTokenExpired() {
        return System.currentTimeMillis() > videoTokenExpiredTime;
    }

}
