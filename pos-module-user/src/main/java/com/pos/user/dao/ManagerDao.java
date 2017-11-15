/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.user.dto.manager.ManagerDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 管理员相关DAO
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Repository
public interface ManagerDao {

    /**
     * 根据用户id获取管理员信息
     *
     * @param userId 管理员userId
     * @return 管理员信息
     */
    ManagerDto getByUserId(@Param("userId") Long userId);
}
