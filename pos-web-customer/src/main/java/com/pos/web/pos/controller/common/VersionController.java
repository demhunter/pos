/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.common;

import com.pos.basic.dto.version.VersionDto;
import com.pos.basic.dto.version.VersionInstructionDto;
import com.pos.basic.exception.BasicErrorCode;
import com.pos.basic.gate.AppVersionConfigService;
import com.pos.basic.gate.dto.AppVersionConfigDto;
import com.pos.basic.service.VersionInstructionService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.web.pos.vo.version.VersionVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Version;

import javax.annotation.Resource;

/**
 * 版本控制相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
@RestController
@RequestMapping("/version")
@Api(value = "/version", description = "v2.0.0 * 版本控制相关接口")
public class VersionController {

    @Resource
    private VersionInstructionService versionInstructionService;

    @Resource
    private AppVersionConfigService appVersionConfigService;

    @RequestMapping(value = "latest", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取最新版本信息", notes = "获取最新版本信息")
    public ApiResult<VersionVo> getLatestVersion(
            @ApiParam(name = "currentVersion", value = "客户端当前版本(xx.xx.xx)")
            @RequestParam("currentVersion") String currentVersion) {
        VersionDto version = VersionDto.parse(currentVersion);
        AppVersionConfigDto versionConfig = appVersionConfigService.getConfig();
        if (versionConfig == null) {
            return ApiResult.fail(BasicErrorCode.VERSION_CONFIG_ERROR_NOT_EXISTED);
        }

        VersionDto latest = versionConfig.parseCurrentVersion();
        VersionDto minimum = versionConfig.parseMinVersion();

        VersionVo result = new VersionVo();
        result.setCurrentVersion(version.toString());
        result.setLatestVersion(latest.toString());
        result.setNeedUpdate(version.compare(latest, false) < 0);
        if (result.getNeedUpdate()) {
            result.setForceUpdate(version.compare(minimum, false) < 0);
            result.setLatestAndroidUrl(versionConfig.getLatestAndroidUrl());
            result.setLatestAndroidMD5(versionConfig.getLatestAndroidMd5());

            VersionInstructionDto instruction = versionInstructionService.findInstruction(latest, true);
            if (instruction != null) {
                result.setUpdateInstruction(instruction.getInstruction());
            }
        }

        return ApiResult.succ(result);
    }
}
