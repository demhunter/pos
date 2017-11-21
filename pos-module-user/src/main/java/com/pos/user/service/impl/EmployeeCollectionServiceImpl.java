/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dao.EmployeeCollectionDao;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dao.EmployeeDao;
import com.pos.user.domain.EmployeeCollection;
import com.pos.user.service.EmployeeCollectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 业者用户收藏相关Service实现
 *
 * @author wangbing
 * @version 1.0, 2017/01/03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeCollectionServiceImpl implements EmployeeCollectionService {

    @Resource
    private EmployeeCollectionDao employeeCollectionDao;

    @Resource
    private EmployeeDao employeeDao;

    @Override
    public boolean updateCollection(Long employeeId, UserIdentifier user) {
        FieldChecker.checkEmpty(employeeId, "employeeId");
        FieldChecker.checkEmpty(user, "user");

        EmployeeCollection collection = employeeCollectionDao.queryCollection(employeeId, user);
        boolean isSuccess = false;
        if (collection == null) {
            // 没有收藏过该业者
            Date now = new Date();
            collection = new EmployeeCollection();
            collection.setEmployeeId(employeeId);
            collection.setUserId(user.getUserId());
            collection.setUserType(user.getUserType());
            collection.setCreateTime(now);
            collection.setUpdateTime(now);
            employeeCollectionDao.saveCollection(collection);
            isSuccess = true;
        } else if (!collection.isAvailable()) {
            // 收藏过该业者，但有取消收藏
            collection.setAvailable(Boolean.TRUE);
            collection.setUpdateTime(new Date());
            employeeCollectionDao.updateCollection(collection);
            isSuccess = true;
        }

        return isSuccess;
    }

    @Override
    public boolean cancelCollection(Long employeeId, UserIdentifier user) {
        FieldChecker.checkEmpty(employeeId, "employeeId");
        FieldChecker.checkEmpty(user, "user");

        EmployeeCollection collection = employeeCollectionDao.queryCollection(employeeId, user);
        boolean isSuccess = false;
        if (collection != null && collection.isAvailable()) {
            collection.setAvailable(Boolean.FALSE);
            collection.setUpdateTime(new Date());
            employeeCollectionDao.updateCollection(collection);
            isSuccess = true;
        }

        return isSuccess;
    }

    @Override
    public Pagination<EmployeeDto> queryCollectionList(Long userId, String userType, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        FieldChecker.checkEmpty(userType, "userType");

        int collectionCount = employeeCollectionDao.queryCollectionCount(userId, userType);
        Pagination<EmployeeDto> pagination = Pagination.newInstance(limitHelper, collectionCount);
        if (collectionCount > 0) {
            pagination.setResult(employeeDao.queryCollectionEmployees(userId, userType, limitHelper));
        }

        return pagination;
    }

    @Override
    public Boolean isCollected(Long employeeId, UserIdentifier user) {
        FieldChecker.checkEmpty(employeeId, "employId");
        FieldChecker.checkEmpty(user, "user");

        EmployeeCollection collection = employeeCollectionDao.queryCollection(employeeId, user);

        return collection == null ? false : collection.isAvailable();
    }
}
