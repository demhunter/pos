/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.pos;

import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.pos.dto.request.BindCardDto;
import com.pos.pos.dto.user.PosUserIdentityDto;
import com.pos.pos.service.PosService;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 身份认证相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@RestController
@RequestMapping("/verification")
@Api(value = "/verification", description = "v1.0.0 * POS身份认证相关接口")
public class VerificationController {

    @Resource
    private PosService posService;

    @RequestMapping(value = "identity", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取已提交的身份认证信息", notes = "获取已提交的身份认证信息（PS：从未提交过身份认证信息则返回空）")
    public ApiResult<PosUserIdentityDto> getIdentityInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posService.getIdentityInfo(userInfo.getId(), false));
    }

    @RequestMapping(value = "identity", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 提交身份认证信息-1", notes = "提交身份认证信息-1")
    public ApiResult<NullObject> updateIdentityInfo(
            @ApiParam(name = "identityInfo", value = "身份认证信息")
            @RequestBody PosUserIdentityDto identityInfo,
            @FromSession UserInfo userInfo) {
        return posService.updateIdentityInfo(userInfo.getId(), identityInfo);
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取绑定的收款卡信息，身份认证信息-2", notes = "获取绑定的收款卡信息，身份认证信息-2（PS：从未绑定过收款银行卡则返回空）")
    public ApiResult<BindCardDto> getBindCardInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posService.getBindCardInfo(userInfo.getId(), false));
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 绑定收款卡，提交身份认证信息-2", notes = "绑定收款银行卡，提交身份认证信息-2")
    public ApiResult<NullObject> bingCard(
            @ApiParam(name = "bindCardInfo", value = "绑卡信息")
            @RequestBody BindCardDto bindCardInfo,
            @FromSession UserInfo userInfo) {
        return posService.bindCard(bindCardInfo, userInfo.getId());
    }
}
