/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.transaction.dto.develop.PosUserChildChannelDto;
import com.pos.transaction.domain.UserPosChannelInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Repository
public interface PosUserChannelDao {

    /**
     * 新增渠道商
     *
     * @param channel
     */
    void save(@Param("channel") UserPosChannelInfo channel);

    /**
     * 根据渠道商用户id，获取渠道商信息
     *
     * @param channelUserId 渠道商用户id
     * @return 渠道商信息
     */
    UserPosChannelInfo get(@Param("channelUserId") Long channelUserId);

    /**
     * 根据id获取推客信息
     *
     * @param id 记录id
     * @return 推客信息
     */
    UserPosChannelInfo getById(@Param("id") Long id);

    /**
     * 更新渠道商信息
     *
     * @param channel 渠道商信息
     */
    void update(@Param("channel") UserPosChannelInfo channel);

    /**
     * 查询用户发展的下级推客数量
     *
     * @param parentUserId 推客userId
     * @return 下级推客数量
     */
    int getUserDevelopCount(@Param("parentUserId") Long parentUserId);

    /**
     * 查询用户发展的下级推客列表
     *
     * @param parentUserId 用户id
     * @param limitHelper  分页参数
     * @return 查询结果
     */
    List<PosUserChildChannelDto> queryDevelopChildChannels(
            @Param("parentUserId") Long parentUserId,
            @Param("limitHelper") LimitHelper limitHelper);
}
