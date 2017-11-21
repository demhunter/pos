/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.pos.domain.UserPosJuniorInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangbing
 * @version 1.0, 2017/10/26
 */
@Repository
public interface PosUserJuniorDao {

    void addJuniorInfo(@Param("juniorInfo") UserPosJuniorInfo userPosJuniorInfo);

    UserPosJuniorInfo getJuniorByJuniorUserId(@Param("juniorUserId") Long juniorUserId);

    void updateJunior(@Param("juniorInfo") UserPosJuniorInfo userPosJuniorInfo);

    List<UserPosJuniorInfo> queryJuniorsByChannelUserId(@Param("channelUserId") Long channelUserId);
}
