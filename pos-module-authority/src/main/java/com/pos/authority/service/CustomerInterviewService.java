/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service;

import com.pos.authority.dto.interview.CustomerInterviewDto;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;

import java.util.List;

/**
 * 客户回访Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public interface CustomerInterviewService {

    /**
     * 新增回访记录
     *
     * @param interview 回访记录信息
     * @param operator  操作人标识
     * @return 操作结果
     */
    ApiResult<NullObject> addInterview(CustomerInterviewDto interview, UserIdentifier operator);

    /**
     * 查询指定用户的回访记录
     *
     * @param userId 用户id
     * @return 回访记录
     */
    ApiResult<List<CustomerInterviewDto>> queryInterviews(Long userId);
}
