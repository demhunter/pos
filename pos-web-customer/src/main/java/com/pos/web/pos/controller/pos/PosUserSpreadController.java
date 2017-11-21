/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.pos;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import com.pos.pos.dto.spread.SpreadGeneralInfoDto;
import com.pos.pos.service.PosUserChannelInfoService;
import com.pos.user.session.UserInfo;
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
@RequestMapping(value = "/pos/spread")
@Api(value = "/pos/spread", description = "v1.0.0 * 发展收款客户相关接口")
public class PosUserSpreadController {

    @Resource
    private PosUserChannelInfoService posUserChannelInfoService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 发展收款客户概要信息", notes = "发展收款客户概要信息")
    public ApiResult<SpreadGeneralInfoDto> getSpreadGeneralInfo(
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.getSpreadGeneralInfo(userInfo.buildUserIdentifier());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 发展收款客户列表", notes = "发展收款客户列表")
    public ApiResult<Pagination<SpreadCustomerDto>> querySpreadCustomers(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.querySpreadCustomers(
                userInfo.getId(), LimitHelper.create(pageNum, pageSize));
    }
}
