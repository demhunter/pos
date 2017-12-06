/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.authentication;

import com.pos.authority.dto.identity.CustomerIdentityDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.service.PosCardService;
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
 * 身份认证和结算卡绑定相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/authentication")
@Api(value = "/authentication", description = "v2.0.0 * 身份认证和结算卡绑定相关接口")
public class AuthenticationController {

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private PosCardService posCardService;

    @RequestMapping(value = "identity", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取已提交的身份认证信息", notes = "获取已提交的身份认证信息（v2.0.0 不传持证照）")
    public ApiResult<CustomerIdentityDto> getIdentityInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(customerAuthorityService.getCustomerIdentity(userInfo.getId()));
    }

    @RequestMapping(value = "identity", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 提交身份认证信息-1", notes = "提交身份认证信息-1（v2.0.0 不传持证照）")
    public ApiResult<NullObject> updateIdentityInfo(
            @ApiParam(name = "identityInfo", value = "身份认证信息")
            @RequestBody CustomerIdentityDto identityInfo,
            @FromSession UserInfo userInfo) {
        return customerAuthorityService.updateCustomerIdentity(userInfo.getId(), identityInfo);
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取绑定的收款卡信息，身份认证信息-2", notes = "获取绑定的收款卡信息，身份认证信息-2（PS：从未绑定过收款银行卡则返回空）")
    public ApiResult<BindCardDto> getBindCardInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posCardService.getWithdrawCard(userInfo.getId()));
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 绑定收款卡，提交身份认证信息-2", notes = "绑定收款银行卡，提交身份认证信息-2")
    public ApiResult<NullObject> bingCard(
            @ApiParam(name = "bindCardInfo", value = "绑卡信息")
            @RequestBody BindCardDto bindCardInfo,
            @FromSession UserInfo userInfo) {
        // TODO 绑卡操作日志
        return posCardService.bindWithdrawCard(userInfo.getId(), bindCardInfo);
    }
}
