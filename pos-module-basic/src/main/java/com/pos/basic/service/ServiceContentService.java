/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.condition.query.ServiceContentCondition;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.basic.dto.ServiceContentDto;

/**
 * 服务内容Service
 *
 * @author cc
 * @version 1.0, 2016/11/18
 */
public interface ServiceContentService {

    /**
     * 查询阶段服务信息列表
     *
     * @param condition 查询条件
     * @param limitHelper 分页参数
     * @return 查询结果
     */
    Pagination<ServiceContentDto> queryServiceContents(ServiceContentCondition condition, LimitHelper limitHelper);

    /**
     * 查询指定阶段服务内容的信息
     *
     * @param condition 服务ID
     * @return 查询结果
     */
    ServiceContentDto getServiceContent(ServiceContentCondition condition);

    /**
     * 新增阶段服务信息
     *
     * @param serviceContentDto 阶段服务信息
     * @return 执行结果
     */
    ApiResult addServiceContent(ServiceContentDto serviceContentDto);

    /**
     * 更新阶段服务信息
     *
     * @param serviceContentDto 阶段服务信息
     * @return 执行结果
     */
    ApiResult updateServiceContent(ServiceContentDto serviceContentDto);
}
