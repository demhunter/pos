/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM用户信息的DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/8
 */
@Repository
public interface UserInfoDao {

    void save(@Param("ui") UserInfo userInfo);

    void update(@Param("ui") UserInfo userInfo);

    void updateByUser(@Param("ui") UserInfo userInfo);

    UserInfo find(@Param("userId") Long userId,
                  @Param("userType") String userType, @Param("available") Boolean available);

    UserInfo findByPhone(@Param("phone") String phone,
                         @Param("userType") String userType, @Param("available") Boolean available);

    List<UserInfo> findUsers(@Param("usersId") List<Long> usersId,
                       @Param("userType") String userType, @Param("available") Boolean available);

}
