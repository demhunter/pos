/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.brokerage;

import com.pos.common.util.mvc.support.Pagination;
import com.pos.web.pos.vo.brokerage.BrokerageAppliedRecordVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.transaction.dto.brokerage.BrokerageDailyStatisticsDto;
import com.pos.transaction.dto.brokerage.BrokerageGeneralInfoDto;
import com.pos.transaction.service.PosUserBrokerageRecordService;
import com.pos.transaction.service.PosUserChannelInfoService;
import com.pos.user.session.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 佣金相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
@RestController
@RequestMapping("/brokerage")
@Api(value = "/brokerage", description = "v2.0.0 * 佣金相关接口")
public class BrokerageController {

    @Resource
    private PosUserChannelInfoService posUserChannelInfoService;

    @Resource
    private PosUserBrokerageRecordService posUserBrokerageRecordService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取佣金简要统计信息", notes = "获取佣金简要统计信息")
    public ApiResult<BrokerageGeneralInfoDto> getTwitterGeneralInfo(
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.queryTwitterGeneralInfo(userInfo.getId());
    }

    @RequestMapping(value = "daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取每日收益记录", notes = "获取每日收益记录")
    public ApiResult<List<BrokerageDailyStatisticsDto>> queryDailyStatistics(
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
    @ApiOperation(value = "v2.0.0 * 提现记录", notes = "提现记录")
    public ApiResult<Pagination<BrokerageAppliedRecordVo>> queryWithdrawDepositRecord(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return null;
    }
}
