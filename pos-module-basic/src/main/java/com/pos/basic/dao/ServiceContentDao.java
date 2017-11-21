/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.condition.query.ServiceContentCondition;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.basic.domain.ServiceContent;
import com.pos.basic.dto.ServiceContentDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务内容Dao
 *
 * @author cc
 * @version 1.0, 2016/11/18
 */
@Repository
public interface ServiceContentDao {

    /**
     * 查询阶段服务内容列表
     *
     * @param condition 查询条件
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    List<ServiceContent> queryServiceContents(
            @Param("condition") ServiceContentCondition condition,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询符合条件的阶段服务内容数量
     *
     * @param condition 查询条件
     * @return 查询结果
     */
    int getServiceContentCount(@Param("condition") ServiceContentCondition condition);

    /**
     * 查询指定ID的阶段服务内容信息
     *
     * @param condition 服务ID
     * @return ServiceContentDto
     */
    ServiceContentDto getServiceContent(@Param("condition") ServiceContentCondition condition);

    /**
     * 新增阶段服务内容
     *
     * @param serviceContent 阶段服务内容信息
     */
    void save(@Param("service") ServiceContent serviceContent);

    /**
     * 更新阶段服务内容信息
     *
     * @param serviceContent 阶段服务内容信息
     */
    void update(@Param("service") ServiceContent serviceContent);
}
