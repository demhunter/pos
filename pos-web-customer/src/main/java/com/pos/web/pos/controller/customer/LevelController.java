/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.customer;

import com.pos.authority.dto.level.CustomerLevelConfigDto;
import com.pos.authority.dto.level.CustomerUpgradeLevelDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.transaction.dto.CreateOrderDto;
import com.pos.transaction.dto.request.LevelUpgradeDto;
import com.pos.transaction.service.PosService;
import com.pos.user.session.UserInfo;
import com.pos.web.pos.vo.level.LevelUpgradeVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 等级相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/level")
@Api(value = "/level", description = "v2.0.0 等级相关接口")
public class LevelController {

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private PosService posService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取等级列表和当前用户等级", notes = "获取等级列表和当前用户等级（v2.0.0 用于“我要升级”页面展示）")
    public ApiResult<List<CustomerLevelConfigDto>> getLevelConfigs(
            @FromSession UserInfo userInfo) {
        return customerAuthorityService.getLevels(userInfo.getId());
    }

    @RequestMapping(value = "upgrade", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 客户支付晋升服务费晋升时，获取当前等级信息和相关限制条件的统计信息", notes = "客户支付晋升服务费晋升时，获取当前等级信息和相关限制条件的统计信息")
    public ApiResult<CustomerUpgradeLevelDto> getLevelInfo(
            @ApiParam(name = "targetLevel", value = "晋升目标等级（可空，表示只查询当前的等级和相关限制条件的统计信息）")
            @RequestParam(name = "targetLevel", required = false) Integer targetLevel,
            @FromSession UserInfo userInfo) {
        return customerAuthorityService.getCustomerUpgradeInfo(userInfo.getId(), targetLevel);
    }
}
