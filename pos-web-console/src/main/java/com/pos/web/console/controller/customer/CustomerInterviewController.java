/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.customer;

import com.pos.authority.dto.interview.CustomerInterviewDto;
import com.pos.authority.service.CustomerInterviewService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户回访相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
@RestController
@RequestMapping("/customer/interview")
@Api(value = "/customer/interview", description = "v2.0.0 * 用户回访相关接口")
public class CustomerInterviewController {

    @Resource
    private CustomerInterviewService customerInterviewService;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取指定用户的回访记录", notes = "获取指定用户的回访记录")
    public ApiResult<List<CustomerInterviewDto>> getCustomerInterview(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        return customerInterviewService.queryInterviews(userId);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 增加指定用户的回访记录", notes = "增加指定用户的回访记录")
    public ApiResult<NullObject> getCustomerInterview(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId,
            @ApiParam(name = "interview", value = "回访信息")
            @RequestBody CustomerInterviewDto interview,
            @FromSession UserInfo userInfo) {
        interview.setUserId(userId);
        return customerInterviewService.addInterview(interview, userInfo.buildUserIdentifier());
    }
}
