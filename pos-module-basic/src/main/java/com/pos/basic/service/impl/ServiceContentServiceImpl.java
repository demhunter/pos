/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.converter.ServiceContentConverter;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Preconditions;
import com.pos.basic.condition.query.ServiceContentCondition;
import com.pos.basic.dao.ServiceContentDao;
import com.pos.basic.domain.ServiceContent;
import com.pos.basic.dto.ServiceContentDto;
import com.pos.basic.service.ServiceContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务内容ServiceImpl
 *
 * @author cc
 * @version 1.0, 2016/11/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceContentServiceImpl implements ServiceContentService {

    @Resource
    private ServiceContentDao serviceContentDao;

    @Override
    public Pagination<ServiceContentDto> queryServiceContents(ServiceContentCondition condition, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(condition, "condition");

        int totalCount = serviceContentDao.getServiceContentCount(condition);
        Pagination<ServiceContentDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<ServiceContent> serviceContents = serviceContentDao.queryServiceContents(condition, limitHelper);
            List<ServiceContentDto> result = serviceContents.stream().map(serviceContent ->
                    ServiceContentConverter.toServiceContentDto(serviceContent)).collect(Collectors.toList());
            pagination.setResult(result);
        }

        return pagination;
    }

    /**
     * 查询指定阶段服务内容的信息
     *
     * @param condition 服务ID
     * @return 查询结果
     */
    @Override
    public ServiceContentDto getServiceContent(ServiceContentCondition condition) {
        FieldChecker.checkEmpty(condition, "condition");

        return serviceContentDao.getServiceContent(condition);
    }

    /**
     * 新增阶段服务信息
     *
     * @param serviceContentDto 阶段服务信息
     * @return 执行结果
     */
    @Override
    public ApiResult addServiceContent(ServiceContentDto serviceContentDto) {
        FieldChecker.checkEmpty(serviceContentDto, "serviceContentDto");
        Preconditions.checkArgsNotNull(
                serviceContentDto.getName(), serviceContentDto.getType());

        // 设置创建和更新时间
        serviceContentDto.setCreateTime(Calendar.getInstance().getTime());
        serviceContentDto.setUpdateTime(serviceContentDto.getCreateTime());
        // 创建阶段服务
        serviceContentDao.save(ServiceContentConverter.toServiceContent(serviceContentDto));
        return ApiResult.succ();
    }

    /**
     * 更新阶段服务信息
     *
     * @param serviceContentDto 阶段服务信息
     * @return 执行结果
     */
    @Override
    public ApiResult updateServiceContent(ServiceContentDto serviceContentDto) {
        FieldChecker.checkEmpty(serviceContentDto, "serviceContentDto");
        Preconditions.checkArgsNotNull(
                serviceContentDto.getName(), serviceContentDto.getType());

        // 设置更新时间
        serviceContentDto.setUpdateTime(Calendar.getInstance().getTime());
        // 更新阶段服务
        serviceContentDao.update(ServiceContentConverter.toServiceContent(serviceContentDto));
        return ApiResult.succ();
    }
}
