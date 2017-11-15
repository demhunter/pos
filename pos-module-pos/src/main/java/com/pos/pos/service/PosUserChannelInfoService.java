/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.pos.domain.Twitter;

/**
 * 快捷收款推客Service
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
public interface PosUserChannelInfoService {

    /**
     * 根据渠道商用户id获取渠道商信息
     *
     * @param channelUserId 渠道商用户id
     * @return 渠道商信息
     */
    Twitter get(Long channelUserId);

}
