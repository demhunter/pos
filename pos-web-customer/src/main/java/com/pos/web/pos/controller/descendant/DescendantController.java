/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.descendant;

import com.pos.authority.dto.statistics.DescendantStatisticsDto;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.session.UserInfo;
import com.pos.web.pos.vo.descendant.ChildInfoVo;
import com.pos.web.pos.vo.descendant.DescendantStatisticsVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 下级相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/descendant")
@Api(value = "/descendant", description = "v2.0.0 * 下级相关接口")
public class DescendantController {

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 下级概要统计", notes = "下级概要统计")
    public ApiResult<DescendantStatisticsDto> getGeneralStatistics(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(customerStatisticsService.getDescendantStatistics(userInfo.getId()));
    }

    @RequestMapping(value = "children", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取直接下级", notes = "获取直接下级")
    public ApiResult<Pagination<ChildInfoVo>> getChildren(
            @ApiParam(name = "childLevel", value = "直接下级等级")
            @RequestParam(name = "childLevel", required = false) Integer childLevel,
            @ApiParam(name = "searchKey", value = "搜索关键字（姓名或备注）")
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "pageNum", value = "页码")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页大小")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return null;
    }

    @RequestMapping(value = "children/{childUserId}/remark", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 给下级添加备注", notes = "给下级添加备注")
    public ApiResult<NullObject> updateChildRemark(
            @ApiParam(name = "childUserId", value = "下级用户id")
            @PathVariable("childUserId") Long childUserId,
            @ApiParam(name = "remark", value = "备注信息")
            @RequestParam("remark") String remark,
            @FromSession UserInfo userInfo) {
        return null;
    }
}
