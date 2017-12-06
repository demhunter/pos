/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.controller.statistics;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.web.posconsole.vo.statistics.StatisticsDailyVo;
import com.pos.web.posconsole.vo.statistics.StatisticsGeneralVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 统计相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/12/3
 */
@RestController
@RequestMapping("/statistics")
@Api(value = "/statistics", description = "v2.0.0 * 统计相关接口")
public class StatisticsController {

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取统计信息总览", notes = "获取统计信息总览")
    public ApiResult<StatisticsGeneralVo> getGeneralStatistics() {
        return null;
    }

    @RequestMapping(value = "daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 查询每日统计数据", notes = "查询每日统计数据")
    public ApiResult<Pagination<StatisticsDailyVo>> queryDailyStatistics(
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        return null;
    }

    @RequestMapping(value = "daily/export", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 查询每日统计数据", notes = "查询每日统计数据")
    public ModelAndView exportDailyStatistics(
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime) {
        return null;
    }
}
