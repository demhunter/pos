/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.bank;

import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.service.PosCardService;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 银行卡管理Controller
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/bank/card")
@Api(value = "/bank/card", description = "v2.0.0 * 佣金相关接口")
public class BankCardController {

    @Resource
    private PosCardService posCardService;

    @RequestMapping(value = "settlement", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取当前绑定的结算银行卡信息", notes = "获取当前绑定的结算银行卡信息")
    public ApiResult<PosCardDto> getSettlementBankCard(
            @FromSession UserInfo userInfo) {
        return posCardService.findWithdrawCard(userInfo.getId());
    }

    @RequestMapping(value = "settlement", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 更换当前绑定的结算银行卡", notes = "更换当前绑定的结算银行卡")
    public ApiResult<NullObject> updateSettlementBankCard(
            @ApiParam(name = "bindCardInfo", value = "绑卡信息")
            @RequestBody BindCardDto bindCardInfo,
            @FromSession UserInfo userInfo) {
        return posCardService.alterWithdrawCard(userInfo.getId(), bindCardInfo);
    }

    @RequestMapping(value = "expenditure", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取当前保存的已使用银行卡列表", notes = "获取当前保存的已使用银行卡列表")
    public ApiResult<List<PosCardDto>> getExpenditureCards(
            @FromSession UserInfo userInfo) {
        return posCardService.queryOutBankCard(userInfo.getId());
    }

    @RequestMapping(value = "expenditure/{cardId}/delete", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 删除已保存的卡信息", notes = "删除已保存的卡信息")
    public ApiResult<NullObject> deleteBankCard(
            @ApiParam(name = "cardId", value = "银行卡id")
            @PathVariable("cardId") Long cardId,
            @FromSession UserInfo userInfo) {
        return posCardService.deleteOutBankCard(cardId, userInfo.getId());
    }
}
