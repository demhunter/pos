/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.pos.domain.TwitterCustomer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangbing
 * @version 1.0, 2017/10/26
 */
@Repository
public interface PosUserJuniorDao {

    void addJuniorInfo(@Param("juniorInfo") TwitterCustomer twitterCustomer);

    TwitterCustomer getJuniorByJuniorUserId(@Param("juniorUserId") Long juniorUserId);

    void updateJunior(@Param("juniorInfo") TwitterCustomer twitterCustomer);

    List<TwitterCustomer> queryJuniorsByChannelUserId(@Param("channelUserId") Long channelUserId);
}
