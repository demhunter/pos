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
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.pos.dto.develop.DevelopGeneralInfoDto;
import com.pos.pos.dto.develop.PosUserChildChannelDto;
import com.pos.pos.service.PosUserChannelInfoService;
import com.pos.user.session.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 发展下级推客相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@RestController
@RequestMapping(value = "/pos/develop")
@Api(value = "/pos/develop", description = "* 发展下级推客相关接口")
public class PosUserDevelopController {

    @Resource
    private PosUserChannelInfoService posUserChannelInfoService;

    @RequestMapping(value = "general", method = RequestMethod.GET)
    @ApiOperation(value = "wb * 获取发展下级推客概要信息", notes = "获取发展下级推客概要信息")
    public ApiResult<DevelopGeneralInfoDto> getDevelopGeneralInfo(
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.getDevelopGeneralInfo(userInfo.getId());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "wb * 发展的下级推客列表", notes = "发展的下级推客列表")
    public ApiResult<Pagination<PosUserChildChannelDto>> queryChildChannels(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize,
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.queryDevelopTwitters(
                userInfo.getId(), LimitHelper.create(pageNum, pageSize));
    }

    @RequestMapping(value = "{developId}/remark", method = RequestMethod.POST)
    @ApiOperation(value = "wb * 编辑下级推客的备注", notes = "编辑下级推客的备注")
    public ApiResult<NullObject> updateChildChannelRemark(
            @ApiParam(name = "developId", value = "下级推客的主键id")
            @PathVariable("developId") Long developId,
            @ApiParam(name = "remark", value = "下级推客备注")
            @RequestParam("remark") String remark,
            @FromSession UserInfo userInfo) {
        return posUserChannelInfoService.updateTwitterRemark(developId, remark, userInfo.getId());
    }
}
