/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.version;

import com.pos.basic.dto.version.VersionInstructionDto;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * 版本更新说明相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
@RestController
@RequestMapping("/version")
@Api(value = "/version", description = "v2.0.0 * 版本更新说明相关接口")
public class VersionInstructionController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取版本更新说明列表", notes = "获取版本更新说明列表")
    public ApiResult<Pagination<VersionInstructionDto>> getVersionInstructions(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        return null;
    }

    @RequestMapping(value = "{versionId}", method =RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取指定版本更新说明", notes = "获取指定版本更新说明")
    public ApiResult<VersionInstructionDto> getVersionInstruction(
            @ApiParam(name = "versionId", value = "版本更新说明id")
            @PathVariable("versionId") Long versionId,
            @FromSession UserInfo userInfo) {
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 新增或更新版本更新说明", notes = "新增或更新版本更新说明")
    public ApiResult<NullObject> saveOrUpdateInstruction(
            @ApiParam(name = "instruction", value = "版本更新说明内容")
            @RequestBody VersionInstructionDto instruction,
            @FromSession UserInfo userInfo) {
        return null;
    }

    @RequestMapping(value = "{versionId}/available", method =RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 启用或禁用版本更新说明", notes = "启用或禁用版本更新说明")
    public ApiResult<NullObject> updateAvailable(
            @ApiParam(name = "versionId", value = "版本更新说明id")
            @PathVariable("versionId") Long versionId,
            @ApiParam(name = "available", value = "true：启用；false：禁用")
            @RequestParam("available") Boolean available,
            @FromSession UserInfo userInfo) {
        return null;
    }
}
