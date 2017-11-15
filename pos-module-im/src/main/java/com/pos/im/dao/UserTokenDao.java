/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.UserToken;
import com.pos.im.dto.session.SessionUserTokenDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户IM Token DAO.
 *
 * @author wayne
 * @version 1.0, 2016/7/7
 */
@Repository
public interface UserTokenDao {

    void save(@Param("ut") UserToken userToken);

    void update(@Param("ut") UserToken userToken);

    UserToken findUserToken(@Param("userId") Long userId, @Param("userType") String userType);

    /**
     * 判断用户是否持有Token，不管Token是否有效.
     *
     * @param userId   用户ID
     * @param userType 用户类型
     * @return
     */
    int hasUserToken(@Param("userId") Long userId, @Param("userType") String userType);

    SessionUserTokenDto findTokenAndUid(@Param("sessionId") Long sessionId);

}
