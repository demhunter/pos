/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.statistics;

import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.mvc.view.XlsStyle;
import com.pos.common.util.mvc.view.XlsView;
import com.pos.transaction.dto.statistics.PosGeneralStatisticsDto;
import com.pos.transaction.dto.statistics.TransactionDailyStatisticsDto;
import com.pos.transaction.service.PosStatisticsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Resource
    private PosStatisticsService posStatisticsService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取统计信息总览", notes = "获取统计信息总览")
    public ApiResult<PosGeneralStatisticsDto> getGeneralStatistics() {
        return posStatisticsService.queryGeneralStatistics();
    }

    @RequestMapping(value = "daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 查询每日统计数据", notes = "查询每日统计数据")
    public ApiResult<Pagination<TransactionDailyStatisticsDto>> queryDailyStatistics(
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        Date beginDate = null;
        Date endDate = null;
        if (StringUtils.isNotEmpty(beginTime)) {
            beginDate = SimpleDateUtils.parseDate(
                    beginTime, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            beginDate = SimpleDateUtils.getDateOfMidNight(beginDate);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            endDate = SimpleDateUtils.parseDate(
                    endTime, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            endDate = SimpleDateUtils.getDateOfMidNight(endDate);
        }
        return posStatisticsService.queryDailyStatistics(beginDate, endDate, LimitHelper.create(pageNum, pageSize));
    }

    @RequestMapping(value = "daily/export", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 查询每日统计数据", notes = "查询每日统计数据")
    public ModelAndView exportDailyStatistics(
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime) {
        Date beginDate = null;
        Date endDate = null;
        if (StringUtils.isNotEmpty(beginTime)) {
            beginDate = SimpleDateUtils.parseDate(
                    beginTime, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            beginDate = SimpleDateUtils.getDateOfMidNight(beginDate);
        }
        if (StringUtils.isNotEmpty(endTime)) {
            endDate = SimpleDateUtils.parseDate(
                    endTime, SimpleDateUtils.DatePattern.YYYY_MM_DD.toString());
            endDate = SimpleDateUtils.getDateOfMidNight(endDate);
        }
        XlsView xlsView;

        List<TransactionDailyStatisticsDto> data = posStatisticsService.queryDailyStatistics(
                beginDate, endDate, LimitHelper.create(1, Integer.MAX_VALUE, false)).getData().getResult();
        if (CollectionUtils.isEmpty(data)) {
            xlsView = new XlsView(0, getRawNames())
                    .setXlsStyle(new XlsStyle().setSheetName("钱刷刷每日统计").setColumnWidth(20));
        } else {
            xlsView = new XlsView(data.size(), getRawNames())
                    .setXlsStyle(new XlsStyle().setSheetName("钱刷刷每日统计").setColumnWidth(20));
            data.forEach(e -> xlsView.addRowValues(getRawValues(e)));
        }

        return new ModelAndView(xlsView);
    }

    private String[] getRawNames() {
        return new String[]{
                "日期",
                "收款人数",
                "收款笔数",
                "收款总额",
                "平台毛利润",
                "佣金提现人数",
                "佣金提现次数",
                "提现手续费支出"};
    }

    private Object[] getRawValues(TransactionDailyStatisticsDto daily) {
        return new Object[]{
                SimpleDateUtils.formatDate(daily.getDateKey(), SimpleDateUtils.DatePattern.YYYY_MM_DD.toString()),
                daily.getPosCustomerCount(),
                daily.getPosTransactionCount(),
                daily.getPosAmount(),
                daily.getBrokerageCustomerCount(),
                daily.getBrokerageWithdrawalTimes(),
                daily.getBrokerageServiceCharge()
        };
    }
}
