/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.common.util.mvc.support.ApiResult;

/**
 * 多媒体上传Service
 *
 * @author wangbing
 * @version 1.0, 2017/10/26
 */
public interface MediaUploadService {

    /**
     * 上传素材到七牛
     *
     * @param data 素材
     * @return 素材七牛地址
     */
    ApiResult<String> uploadMediaStreamToQiNiu(final byte[] data);
}
