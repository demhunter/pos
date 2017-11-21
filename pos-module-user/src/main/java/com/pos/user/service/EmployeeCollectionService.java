/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.dto.employee.EmployeeDto;

/**
 * 业者用户收藏相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/01/03
 */
public interface EmployeeCollectionService {

    /**
     * 收藏指定业者
     *
     * @param employeeId 业者用户的userId
     * @param user       用户标识
     */
    boolean updateCollection(Long employeeId, UserIdentifier user);

    /**
     * 用户取消业者收藏
     *
     * @param employeeId 业者用户userId
     * @param user       用户标识
     */
    boolean cancelCollection(Long employeeId, UserIdentifier user);

    /**
     * 查询用户的业者收藏列表
     *
     * @param userId      用户ID
     * @param userType    用户类型
     * @param limitHelper 分页参数
     * @return 业者收藏列表
     */
    Pagination<EmployeeDto> queryCollectionList(Long userId, String userType, LimitHelper limitHelper);

    /**
     * 查询用户是否收藏该业者
     *
     * @param employeeId 业者用户的userId
     * @param user       用户标识
     * @return true：已收藏，false：未收藏
     */
    Boolean isCollected(Long employeeId, UserIdentifier user);
}
