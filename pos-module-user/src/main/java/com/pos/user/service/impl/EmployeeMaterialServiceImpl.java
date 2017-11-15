/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.constant.InvitationChannel;
import com.pos.user.condition.query.RecommendCondition;
import com.pos.user.dao.EmployeeMaterialDao;
import com.pos.user.domain.EmployeeMaterial;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.employee.EmployeeMaterialRelationDto;
import com.pos.user.dto.employee.RecommendDto;
import com.pos.user.service.EmployeeMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 客户业者主材邀请关系相关Service
 *
 * @author lifei
 * @version 1.0, 2017/7/5
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeMaterialServiceImpl implements EmployeeMaterialService{

    @Resource
    private EmployeeMaterialDao employeeMaterialDao;

    @Override
    public EmployeeMaterialRelationDto findRelationByCustomerId(Long customerId) {
        FieldChecker.checkEmpty(customerId, "customerId");

        EmployeeMaterialRelationDto employeeMaterialRelationDto = new EmployeeMaterialRelationDto();
        EmployeeMaterial employeeMaterial = employeeMaterialDao.findRelationByCustomerId(customerId);
        if (employeeMaterial == null) {
            return null;
        }
        BeanUtils.copyProperties(employeeMaterial, employeeMaterialRelationDto);
        return employeeMaterialRelationDto;
    }

    @Override
    public void saveEmployeeMaterial(CustomerDto customerDto, EmployeeDto employeeDto, InvitationChannel channel) {
        FieldChecker.checkEmpty(customerDto, "customerDto");
        FieldChecker.checkEmpty(employeeDto, "employeeDto");

        EmployeeMaterial employeeMaterial = new EmployeeMaterial();
        employeeMaterial.setEmployeeUserId(employeeDto.getId());
        employeeMaterial.setCustomerUserId(customerDto.getId());
        employeeMaterial.setCreateDate(new Date());
        employeeMaterial.setChannel(channel.getCode());
        employeeMaterialDao.saveEmployeeMaterial(employeeMaterial);
    }

    @Override
    public Pagination<RecommendDto> getRecommendList(RecommendCondition recommendCondition, LimitHelper limitHelper, OrderHelper orderHelper) {
        int recommendCount = employeeMaterialDao.getRecommendListCount(recommendCondition);
        Pagination<RecommendDto> pagination = Pagination.newInstance(limitHelper, recommendCount);
        if (recommendCount > 0) {
            pagination.setResult(employeeMaterialDao.getRecommendList(recommendCondition, limitHelper,orderHelper));
        }
        return pagination;
    }
}
