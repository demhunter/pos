/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.pos.authority.dao.CustomerInterviewDao;
import com.pos.authority.dto.interview.CustomerInterviewDto;
import com.pos.authority.service.CustomerInterviewService;
import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户回访ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerInterviewServiceImpl implements CustomerInterviewService {

    @Resource
    private CustomerInterviewDao customerInterviewDao;

    @Override
    public ApiResult<NullObject> addInterview(CustomerInterviewDto interview, UserIdentifier operator) {
        FieldChecker.checkEmpty(interview, "interview");
        FieldChecker.checkEmpty(operator, "operator");
        FieldChecker.checkEmpty(interview.getUserId(), "userId");
        FieldChecker.checkMaxLength(interview.getContent(), 200, "content");

        interview.setCreateUserId(operator.getUserId());

        customerInterviewDao.save(interview);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<List<CustomerInterviewDto>> queryInterviews(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return ApiResult.succ(customerInterviewDao.queryInterviews(userId));
    }
}
