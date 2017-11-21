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
import com.pos.pos.dto.PosUserGetBrokerageRecordDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import com.pos.pos.dto.twitter.TwitterGeneralInfoDto;
import com.pos.pos.service.PosUserBrokerageRecordService;
import com.pos.pos.service.PosUserChannelInfoService;
import com.pos.user.session.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * POS 推客佣金相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
@RestController
@RequestMapping("/pos/twitter")
@Api(value = "/pos/twitter", description = "v1.0.0 * 推客佣金相关接口")
public class PosTwitterController {

    @Resource
    private PosUserChannelInfoService posUserChannelInfoService;

    @Resource
    private PosUserBrokerageRecordService posUserBrokerageRecordService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取推客佣金简要统计信息", notes = "获取推客佣金简要统计信息")
    public ApiResult<TwitterGeneralInfoDto> getTwitterGeneralInfo(
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.queryTwitterGeneralInfo(userInfo.getId());
    }

    @RequestMapping(value = "statistics", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取每日记录", notes = "获取每日记录")
    public ApiResult<List<TwitterDailyStatisticsDto>> queryDailyStatistics(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posUserChannelInfoService.queryTwitterDailyStatistics(
                userInfo.getId(), LimitHelper.create(pageNum, pageSize)));
    }

    @RequestMapping(value = "apply", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 申请提现", notes = "申请提现，不传金额，后端计算，申请提交成功返回申请提现的金额")
    public ApiResult<BigDecimal> addWithdrawDepositApply(
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.applyWithdrawBrokerage(userInfo.buildUserIdentifier());
    }

    @RequestMapping(value = "applied/record", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 提现记录", notes = "提现记录")
    public ApiResult<List<PosUserGetBrokerageRecordDto>> queryWithdrawDepositRecord(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posUserBrokerageRecordService.queryBrokerageRecord(
                userInfo.buildUserIdentifier(), LimitHelper.create(pageNum, pageSize)));
    }
}
