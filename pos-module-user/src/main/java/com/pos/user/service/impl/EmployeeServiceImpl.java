/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.condition.query.CompanyEmployeesCondition;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.condition.orderby.EmployeeOrderField;
import com.pos.user.constant.UserType;
import com.pos.user.dao.EmployeeDao;
import com.pos.user.domain.Employee;
import com.pos.user.dto.SessionUserDto;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.dto.employee.EmployeeInfoDto;
import com.pos.user.service.EmployeeService;
import com.pos.user.service.support.UserServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 业者相关Service实现
 *
 * @author cc
 * @version 1.0, 16/8/3
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private UserServiceSupport userServiceSupport;

    @Override
    public EmployeeDto findById(Long userId, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userId, "userId");
        return (EmployeeDto) userServiceSupport.findById(userId, UserType.EMPLOYEE, disable, deleted);
    }

    @Override
    public EmployeeDto findByUserName(String userName, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userName, "userName");
        return (EmployeeDto) userServiceSupport.findByUserName(userName, UserType.EMPLOYEE, disable, deleted);
    }

    @Override
    public EmployeeDto findByUserPhone(String userPhone, boolean disable, boolean deleted) {
        Validator.checkMobileNumber(userPhone);
        return (EmployeeDto) userServiceSupport.findByUserPhone(userPhone, UserType.EMPLOYEE, disable, deleted);
    }

    @Override
    public EmployeeDto findByInvitationCode(String invitationCode, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(invitationCode, "invitationCode");
        return employeeDao.findByInvitationCode(invitationCode, deleted ? null : false, disable ? null : true);
    }

    @Override
    public List<EmployeeDto> findByName(String name, Long companyId,
                                        int maxSize, boolean disable, boolean deleted, boolean quitJob) {
        Validator.checkCnName(name);
        FieldChecker.checkMinMaxValue(maxSize, 1, 50, "maxSize");
        return employeeDao.findEmployeesByName(
                name, companyId, maxSize, deleted ? null : false, disable ? null : true, quitJob);
    }

    @Override
    public List<EmployeeDto> findByNameAndType(String name, Byte employeeType, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(name, "name");
        FieldChecker.checkEmpty(employeeType, "employeeType");

        return employeeDao.findEmployeesByNameAndType(name, employeeType, deleted ? null : false, disable ? null : true);
    }

    @Override
    public List<SessionUserDto> findEmployeeForImMember(String name, String userPhone, String company, Boolean
            deleted, Boolean available, Boolean quitJob) {
        return employeeDao.findEmployeeForImMember(name,userPhone,company,deleted,available, quitJob);
    }

    @Override
    public List<EmployeeDto> findByCompaniesId(
            List<Long> companiesId, int maxSize, boolean disable, boolean deleted, boolean quitjob) {
        FieldChecker.checkEmpty(companiesId, "companiesId");
        FieldChecker.checkMinMaxValue(maxSize, 1, 50, "maxSize");
        return employeeDao.findEmployeesByCompaniesId(companiesId.toArray(
                new Long[companiesId.size()]), maxSize, deleted ? null : false, disable ? null : true, quitjob);
    }

    @Override
    public Pagination<EmployeeDto> queryEmployees(
            CompanyEmployeesCondition condition, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(EmployeeOrderField.getInterface());
        }

        int totalCount = employeeDao.getTotalEmployeeCount(condition);
        Pagination pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            pagination.setResult(employeeDao.getEmployees(condition, limitHelper, orderHelper));
        }

        return pagination;
    }

    @Override
    public int getTotalEmployeeCount(CompanyEmployeesCondition condition) {
        FieldChecker.checkEmpty(condition, "condition");
        return employeeDao.getTotalEmployeeCount(condition);
    }

    @Override
    public boolean update(EmployeeDto beforeDto, EmployeeDto afterDto) {
        userServiceSupport.checkUpdate(beforeDto, afterDto);
        boolean isUpdated = userServiceSupport.updateUser(beforeDto, afterDto);
        Employee before = UserDtoConverter.convert2Employee(beforeDto);
        Employee after = UserDtoConverter.convert2Employee(afterDto);
        if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
            employeeDao.update(after);
            isUpdated = true;
        }

        // 更新用户类型信息, 如果User或Customer被更新, 则强制更新UserClass
        isUpdated = userServiceSupport.updateUserClass(beforeDto, afterDto, isUpdated);
        // 更新后将用户信息刷新到IMServer
        if (isUpdated) {
            // userServiceSupport.refresh2IMServer(beforeDto, afterDto);
        }

        return isUpdated;
    }

    /**
     * 查询指定的一组业者信息.
     *
     * @param userIds 一组用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    @Override
    public List<EmployeeDto> findInUserIds(List<Long> userIds, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userIds, "userIds");
        if(CollectionUtils.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return employeeDao.findInUserIds(userIds, deleted ? null : false, disable ? null : true);
    }

    @Override
    public List<EmployeeInfoDto> findByUserDetailType(Byte userType, Long companyId, Long userId, boolean quitJob, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userType, "userType");
        FieldChecker.checkEmpty(companyId, "companyId");
        FieldChecker.checkEmpty(userId, "userId");

        List<EmployeeInfoDto> employeeInfoDtos = employeeDao.findByUserDetailType(userType, companyId,  quitJob ? null : false,  disable ? null : true,  deleted ? null : false);
        List<EmployeeInfoDto> employeeInfoDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(employeeInfoDtos)){
            employeeInfoDtos.forEach(employeeInfoDto -> {
                if (!employeeInfoDto.getUserId().equals(userId)){
                    employeeInfoDtoList.add(employeeInfoDto);
                }
            });
        }
        return employeeInfoDtoList;
    }
}
