/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.basic.condition.query.PlatformStatisticsCondition;
import com.pos.im.domain.Session;
import com.pos.im.dto.session.SessionDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台数据统计-IM会话数据统计Dao
 *
 * @author wangbing
 * @version 1.0, 2017/03/30
 */
@Deprecated
public interface IMPlatformStatisticsDao {

    /**
     * 查询IM会话信息
     *
     * @param condition 查询条件
     * @return
     */
    List<Session> queryIMSessionInfo(@Param("condition") PlatformStatisticsCondition condition);

    /**
     * 查询与作品有关的IM会话信息
     *
     * @param condition 查询条件
     * @return
     */
    List<SessionDto> queryIMSessionInfoAboutCase(@Param("condition") PlatformStatisticsCondition condition);

    /**
     * 查询IM会话数量
     *
     * @param condition 查询条件
     * @return
     */
    Integer queryIMCount(@Param("condition") PlatformStatisticsCondition condition);
}
