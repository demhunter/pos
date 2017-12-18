/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.gate.controller;

import com.pos.basic.dto.version.VersionDto;
import com.pos.basic.exception.BasicErrorCode;
import com.pos.basic.gate.AppVersionConfigService;
import com.pos.basic.gate.dto.AppVersionConfigDto;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.web.gate.vo.ServerInfoVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 服务查询Controller
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
@RestController
@RequestMapping("/service")
@Api(value = "/service", description = "v2.0.0 * 服务查询相关接口")
public class ServiceQueryController {

    @Resource
    private AppVersionConfigService appVersionConfigService;

    @RequestMapping(value = "server", method = RequestMethod.GET)
    @ApiOperation(value = "获取服务器信息", notes = "查询指定APP版本的服务器信息")
    public ApiResult<ServerInfoVo> getServer(
            @ApiParam(name = "version", value = "APP当前版本号，格式：xx.xx.xx，xx取值范围：0-99")
            @RequestParam("version") String version) {
        AppVersionConfigDto versionConfig = appVersionConfigService.getConfig();
        if (versionConfig == null) {
            return ApiResult.fail(BasicErrorCode.VERSION_CONFIG_ERROR_NOT_EXISTED);
        }
        ServerInfoVo result = new ServerInfoVo();
        VersionDto latest = versionConfig.parseCurrentVersion();

        VersionDto appVersion = VersionDto.parse(version);
        result.setServerUrl(versionConfig.getCurrentUrl());
        if (appVersion.compare(latest, false) > 0) {
            result.setServerUrl(versionConfig.getLatestUrl());
        }
        result.setCurrentVersion(appVersion.toString());

        return ApiResult.succ(result);
    }
}
