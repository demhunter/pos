/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.dto.interview.CustomerInterviewDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户回访Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
@Repository
public interface CustomerInterviewDao {

    /**
     * 保存回访信息
     *
     * @param interview 回访信息
     */
    void save(@Param("interview") CustomerInterviewDto interview);

    /**
     * 查询指定用户的回访记录
     *
     * @param userId 用户id
     * @return 回访记录
     */
    List<CustomerInterviewDto> queryInterviews(@Param("userId") Long userId);
}
