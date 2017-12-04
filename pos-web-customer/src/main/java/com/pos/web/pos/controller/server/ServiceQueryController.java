/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.server;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.web.pos.vo.server.ServerInfoVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务查询Controller
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
@RestController
@RequestMapping("/gate")
@Api(value = "/gate", description = "v2.0.0 * Gate获取BaseUrl服务查询相关接口")
public class ServiceQueryController {

    @RequestMapping(value = "server", method = RequestMethod.GET)
    @ApiOperation(value = "获取服务器信息", notes = "查询指定APP版本的服务器信息")
    public ApiResult<ServerInfoVo> getServer(
            @ApiParam(name = "version", value = "APP当前版本号，格式：xx.xx.xx，xx取值范围：0-99")
            @RequestParam("version") String version) {
        return null;
    }
}
