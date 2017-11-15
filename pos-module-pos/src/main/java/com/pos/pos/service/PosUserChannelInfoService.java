/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.pos.domain.Twitter;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;

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

    /**
     * 查询用户发展的渠道商数量
     *
     * @param userId 用户id
     * @return 渠道商数量
     */
    Integer getUserDevelopCount(Long userId);
}
