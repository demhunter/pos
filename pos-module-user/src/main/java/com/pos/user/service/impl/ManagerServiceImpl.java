/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.condition.orderby.ManagerOrderField;
import com.pos.user.constant.UserType;
import com.pos.user.dto.manager.ManagerDto;
import com.pos.user.condition.query.ManagerListCondition;
import com.pos.user.dao.ManagerDao;
import com.pos.user.domain.Manager;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.service.ManagerService;
import com.pos.user.service.support.UserServiceSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 平台管理员相关服务接口的实现类.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private ManagerDao managerDao;

    @Resource
    private UserServiceSupport userServiceSupport;

    @Override
    public ManagerDto findById(Long userId, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userId, "userId");
        return (ManagerDto) userServiceSupport.findById(userId, UserType.MANAGER, disable, deleted);
    }

    @Override
    public ManagerDto findByUserName(String userName, boolean disable, boolean deleted) {
        FieldChecker.checkEmpty(userName, "userName");
        return (ManagerDto) userServiceSupport.findByUserName(userName, UserType.MANAGER, disable, deleted);
    }

    @Override
    public ManagerDto findByUserPhone(String userPhone, boolean disable, boolean deleted) {
        Validator.checkMobileNumber(userPhone);
        return (ManagerDto) userServiceSupport.findByUserPhone(userPhone, UserType.MANAGER, disable, deleted);
    }

    @Override
    public boolean update(ManagerDto beforeDto, ManagerDto afterDto) {
        userServiceSupport.checkUpdate(beforeDto, afterDto);
        boolean isUpdated = userServiceSupport.updateUser(beforeDto, afterDto);
        Manager before = UserDtoConverter.convert2Manager(beforeDto);
        Manager after = UserDtoConverter.convert2Manager(afterDto);
        if (!JsonUtils.objectToJson(before).equals(JsonUtils.objectToJson(after))) {
            managerDao.update(after);
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

    @Override
    public ApiResult<Pagination<ManagerDto>> findManagers(
            ManagerListCondition condition, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(ManagerOrderField.getInterface());
        }

        int totalCount = managerDao.getTotal(condition);
        Pagination<ManagerDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<ManagerDto> list = managerDao.findManagers(condition, limitHelper, orderHelper);
            pagination.setResult(list);
        }

        return ApiResult.succ(pagination);
    }

}