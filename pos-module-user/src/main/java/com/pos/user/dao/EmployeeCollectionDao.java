/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.basic.dto.UserIdentifier;
import com.pos.user.domain.EmployeeCollection;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 业者-用户收藏相关Dao
 *
 * @author wangbing
 * @version 1.0, 2016/12/29
 */
@Repository
public interface EmployeeCollectionDao {

    /**
     * 新增用户-业者收藏关系
     *
     * @param collection 用户-业者收藏关系
     */
    void saveCollection(@Param("collection") EmployeeCollection collection);

    /**
     * 更新用户-业者收藏关系(包括：收藏和取消收藏)
     *
     * @param collection 用户-业者收藏关系
     */
    void updateCollection(@Param("collection") EmployeeCollection collection);

    /**
     * 查询用户收藏的业者数量
     *
     * @param userId 用户ID
     * @param userType 用户类型
     * @return 业者收藏数量
     */
    int queryCollectionCount(@Param("userId") Long userId, @Param("userType") String userType);

    /**
     * 业者-用户是否收藏业者
     *
     * @param employeeId 业者用户的userId
     * @param user 用户标识
     * @return 业者-用户收藏关系
     */
    EmployeeCollection queryCollection(@Param("employeeId") Long employeeId, @Param("user") UserIdentifier user);
}
