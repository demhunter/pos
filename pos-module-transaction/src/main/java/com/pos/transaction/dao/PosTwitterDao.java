/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.transaction.dto.spread.SpreadCustomerDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * POS 推客相关Dao
 *
 * @author wangbing
 * @version 1.0, 2017/10/21
 */
@Repository
public interface PosTwitterDao {

    /**
     * 查询推客发展的有效客户数
     *
     * @param channelUserId 推客用户userId
     * @return 发展的客户数
     */
    int queryChannelJuniorCount(@Param("channelUserId") Long channelUserId);

    /**
     * 查询推客用户发展的客户列表
     *
     * @param channelUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 推客用户发展的客户列表
     */
    List<SpreadCustomerDto> queryChannelJuniors(@Param("channelUserId") Long channelUserId, @Param("limitHelper") LimitHelper limitHelper);
}
