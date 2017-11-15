/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.twitter;

import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.pos.service.TwitterService;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发展收款客户相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@RestController
@RequestMapping(value = "/twitter/spread")
@Api(value = "/twitter/spread", description = "* 发展收款客户相关接口")
public class SpreadController {

    @Resource
    private TwitterService twitterService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "wb 发展收款客户概要信息", notes = "发展收款客户概要信息")
    public ApiResult<SpreadGeneralInfoDto> getSpreadGeneralInfo(
            @FromSession UserInfo userInfo) {
        return twitterService.getSpreadGeneralInfo(userInfo.buildUserIdentifier());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "wb * 发展收款客户列表", notes = "发展收款客户列表")
    public ApiResult<Pagination<SpreadCustomerDto>> querySpreadCustomers(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return twitterService.querySpreadCustomers(
                userInfo.getId(), LimitHelper.create(pageNum, pageSize));
    }
}
