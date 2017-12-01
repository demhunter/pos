/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.controller.customer;

import com.pos.basic.dto.interview.CustomerInterviewDto;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取指定用户的回访记录", notes = "获取指定用户的回访记录")
    public ApiResult<List<CustomerInterviewDto>> getCustomerInterview(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        return null;
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 增加指定用户的回访记录", notes = "增加指定用户的回访记录")
    public ApiResult<NullObject> getCustomerInterview(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId,
            @ApiParam(name = "interview", value = "回访信息")
            @RequestBody CustomerInterviewDto interview,
            @FromSession UserInfo userInfo) {
        return null;
    }
}
