/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.controller.repair;

import com.wordnik.swagger.annotations.Api;
import com.pos.transaction.service.support.RepairPosData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * POS 数据修复Controller
 *
 * @author wangbing
 * @version 1.0, 2017/10/25
 */
@RestController
@RequestMapping("/repair")
@Api(value = "/repair", description = "v1.0.0 * POS数据修复相关接口")
public class PosRepairController {

    @Resource
    private RepairPosData repairPosData;

    /*@RequestMapping(value = "transaction/out-card-info", method = RequestMethod.GET)
    @ApiOperation(value = "* 清洗交易数据的付款银行卡信息，主要作用为解密SQL变更的付款银行卡信息数据", notes = "清洗交易数据的付款银行卡信息，主要作用为解密SQL变更的付款银行卡信息数据")
    public ApiResult<NullObject> repairPosTransactionOutCardInfo() {
        return repairPosData.repairPosTransactionOutCardInfo();
    }*/
}
