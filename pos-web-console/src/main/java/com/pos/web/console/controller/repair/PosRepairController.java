/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.repair;

import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.data.repair.dao.RepairV2_0_0Dao;
import com.pos.data.repair.v2_0_0.DataRepairV2_0_0;
import com.pos.transaction.service.PosStatisticsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;

/**
 * POS 数据修复Controller
 *
 * @author wangbing
 * @version 1.0, 2017/10/25
 */
@RestController
@RequestMapping("/repair")
@Api(value = "/repair", description = "v2.0.0 * POS数据修复相关接口")
public class PosRepairController {

    @Resource
    private PosStatisticsService posStatisticsService;

    @Resource
    private DataRepairV2_0_0 dataRepairV2_0_0;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @RequestMapping(value = "relation/tree", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化用户关系树", notes = "初始化用户关系树")
    public ApiResult<NullObject> repairRelationTree() {
        customerRelationPoolSupport.initialize();
        return ApiResult.succ();
    }

    @RequestMapping(value = "statistics/daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化每日数据统计", notes = "初始化每日数据统计")
    public ApiResult<NullObject> repairDailyStatistics() {
        posStatisticsService.initializeDailyStatistics();
        return ApiResult.succ();
    }

    @RequestMapping(value = "transaction/brokerage/withdrawal", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 修复已处理的佣金提现交易", notes = "修复已处理的佣金提现交易")
    public ApiResult<List<Long>> repairBrokerageTransaction() {
        return dataRepairV2_0_0.repairBrokerageTransaction();
    }

    @RequestMapping(value = "brokerage/record", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 修复交易分佣记录", notes = "修复交易分佣记录")
    public ApiResult<NullObject> repairCustomerBrokerage() {
         dataRepairV2_0_0.repairTransactionCustomerBrokerage();
         return ApiResult.succ();
    }

    @RequestMapping(value = "statistics/customer", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 修复用户统计数据", notes = "修复用户统计数据")
    public ApiResult<NullObject> repairCustomerStatistics() {
        dataRepairV2_0_0.repairCustomerStatistics();
        return ApiResult.succ();
    }
}
