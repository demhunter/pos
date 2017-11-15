/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.user.domain.DataLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 睿智
 * @version 1.0, 2017/10/19
 */
@Repository
public interface DataLogDao {

    /**
     * 保存账号相关的日志
     * @param dataLog
     */
    void saveAccountLog(@Param("dataLog") DataLog dataLog);

    /**
     * 保存APP相关的日志
     * @param dataLog
     */
    void saveAppLog(@Param("dataLog") DataLog dataLog);

    /**
     * 保存作品相关的日志
     * @param dataLog
     */
    void saveCaseLog(@Param("dataLog") DataLog dataLog);

    /**
     * 保存聊天相关的日志
     * @param dataLog
     */
    void saveChatLog(@Param("dataLog") DataLog dataLog);

    /**
     * 保存折扣相关的日志
     * @param dataLog
     */
    void saveDiscountLog(@Param("dataLog") DataLog dataLog);

    /**
     * 保存保障相关的日志
     * @param dataLog
     */
    void saveGuaranteeLog(@Param("dataLog") DataLog dataLog);

    /**
     * 保存我的相关的日志
     * @param dataLog
     */
    void saveMyLog(@Param("dataLog") DataLog dataLog);
}
