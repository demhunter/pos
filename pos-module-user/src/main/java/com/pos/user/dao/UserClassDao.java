/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.user.domain.UserClass;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户类型相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
@Repository
public interface UserClassDao {

    void save(@Param("uc") UserClass userClass);

    void update(@Param("uc") UserClass userClass);

    void updateLastLoginTime(@Param("id") Long id, @Param("lastLoginTime") Date lastLoginTime);

    Long getClassId(@Param("userId") Long userId, @Param("userType") String userType);

    UserClass findClass(@Param("userId") Long userId, @Param("userType") String userType);

    List<UserClass> findClasses(@Param("userId") Long userId);

    List<UserClass> findUserClasses(
            @Param("userType") String userType,
            @Param("limitHelper") LimitHelper limitHelper);
    /**
     * 更新登录相关信息
     *
     * @param userClass 相关登录信息
     */
    void updateLoginInfo(@Param("uc") UserClass userClass);
}
