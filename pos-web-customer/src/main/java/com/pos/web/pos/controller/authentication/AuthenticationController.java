/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.authentication;

import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.dto.user.PosUserIdentityDto;
import com.pos.transaction.service.PosService;
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
    private PosService posService;

    @RequestMapping(value = "identity", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取已提交的身份认证信息", notes = "获取已提交的身份认证信息（v2.0.0 不传持证照）")
    public ApiResult<PosUserIdentityDto> getIdentityInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posService.getIdentityInfo(userInfo.getId(), false));
    }

    @RequestMapping(value = "identity", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 提交身份认证信息-1", notes = "提交身份认证信息-1（v2.0.0 不传持证照）")
    public ApiResult<NullObject> updateIdentityInfo(
            @ApiParam(name = "identityInfo", value = "身份认证信息")
            @RequestBody PosUserIdentityDto identityInfo,
            @FromSession UserInfo userInfo) {
        return posService.updateIdentityInfo(userInfo.getId(), identityInfo);
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取绑定的收款卡信息，身份认证信息-2", notes = "获取绑定的收款卡信息，身份认证信息-2（PS：从未绑定过收款银行卡则返回空）")
    public ApiResult<BindCardDto> getBindCardInfo(
            @FromSession UserInfo userInfo) {
        return ApiResult.succ(posService.getBindCardInfo(userInfo.getId(), false));
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 绑定收款卡，提交身份认证信息-2", notes = "绑定收款银行卡，提交身份认证信息-2")
    public ApiResult<NullObject> bingCard(
            @ApiParam(name = "bindCardInfo", value = "绑卡信息")
            @RequestBody BindCardDto bindCardInfo,
            @FromSession UserInfo userInfo) {
        return posService.bindCard(bindCardInfo, userInfo.getId());
    }

//    @RequestMapping(value = "updateCard",method = RequestMethod.POST)
//    @ApiOperation(value = "更新绑定的收款卡",notes = "更新绑定的收款卡")
//    public ApiResult<QuickGetMoneyVo> updateBingCard(@RequestBody BindCardRequestDto bindCardRequestDto, @FromSession
//            UserInfo userInfo) {
//        BindCardDto bindCardDto = new BindCardDto();
//        BeanUtils.copyProperties(bindCardRequestDto, bindCardDto);
//        ApiResult apiResult = posService.bindCard(bindCardDto, userInfo.getId(),"UPDATE");
//        if (apiResult.isSucc()) {
//            long userId = userInfo.getId();
//            List<UserPosCard> inCards = posService.queryUserCards(userId, CardUsageEnum.IN_CARD.getCode());
//            List<UserPosCard> outCards = posService.queryUserCards(userId, CardUsageEnum.OUT_CARD.getCode());
//            Map<String, String> logos = posService.getAllBankLogo();
//            ApiResult<PosLoginDto> posLoginDto = posService.getUserBaseInfo(userId);
//            if (posLoginDto.isSucc()) {
//                GetMoneyVo getMoneyVo = new GetMoneyVo();
//                BeanUtils.copyProperties(posLoginDto.getData(), getMoneyVo);
//                getMoneyVo.setHeadImage(globalConstants.posHeadImage);
//                CustomerDto customerDto = customerService.findById(userId, false, false);
//                getMoneyVo.setNickName(StringUtils.isNotBlank(customerDto.getName()) ? customerDto.getName() :
//                        customerDto.getUserPhone());
//                QuickGetMoneyVo quickGetMoneyVo = new QuickGetMoneyVo(inCards, outCards, globalConstants.poundageRate,
//                        globalConstants.poundage, globalConstants.arrival, logos, getMoneyVo);
//                return ApiResult.succ(quickGetMoneyVo);
//            } else {
//                return ApiResult.fail(posLoginDto.getError(), apiResult.getMessage());
//            }
//        } else {
//            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
//        }
//    }
}
