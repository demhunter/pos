/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.repair;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.service.PosStatisticsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @RequestMapping(value = "statistics/daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化每日数据统计", notes = "初始化每日数据统计")
    public ApiResult<NullObject> repairPosTransactionOutCardInfo() {
        posStatisticsService.initializeDailyStatistics();
        return ApiResult.succ();
    }
}
