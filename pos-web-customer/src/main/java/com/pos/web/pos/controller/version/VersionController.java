/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.version;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.web.pos.vo.version.VersionVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "latest", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取最新版本信息", notes = "获取最新版本信息")
    public ApiResult<VersionVo> getLatestVersion(
            @ApiParam(name = "currentVersion", value = "客户端当前版本(xx.xx.xx)")
            @RequestParam("currentVersion") String currentVersion) {
        return null;
    }
}
