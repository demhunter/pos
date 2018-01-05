/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.repair;

import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.data.repair.v2_0_0.DataRepairV2_0_0;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.dto.PosOutCardInfoDto;
import com.pos.transaction.helipay.action.QuickPayApi;
import com.pos.transaction.helipay.vo.QueryOrderResponseVo;
import com.pos.transaction.helipay.vo.QueryOrderVo;
import com.pos.transaction.service.PosStatisticsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * POS 数据修复Controller
 *
 * @author wangbing
 * @version 1.0, 2017/10/25
 */
@RestController
@RequestMapping("/repair")
@Api(value = "/repair", description = "v2.0.0 * POS数据修复相关接口")
public class PosRepairController {

    @Resource
    private PosStatisticsService posStatisticsService;

    @Resource
    private DataRepairV2_0_0 dataRepairV2_0_0;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Resource
    private QuickPayApi quickPayApi;

    @Resource
    private PosConstants posConstants;

    @RequestMapping(value = "relation/tree", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化用户关系树", notes = "初始化用户关系树")
    public ApiResult<NullObject> repairRelationTree() {
        customerRelationPoolSupport.initialize();
        return ApiResult.succ();
    }

    @RequestMapping(value = "statistics/daily", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化每日数据统计", notes = "初始化每日数据统计")
    public ApiResult<NullObject> repairDailyStatistics() {
        posStatisticsService.initializeDailyStatistics();
        return ApiResult.succ();
    }

    @RequestMapping(value = "transaction/brokerage/withdrawal", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 修复已处理的佣金提现交易", notes = "修复已处理的佣金提现交易")
    public ApiResult<List<Long>> repairBrokerageTransaction() {
        return dataRepairV2_0_0.repairBrokerageTransaction();
    }

    @RequestMapping(value = "brokerage/record", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 修复交易分佣记录", notes = "修复交易分佣记录")
    public ApiResult<NullObject> repairCustomerBrokerage() {
        dataRepairV2_0_0.repairTransactionCustomerBrokerage();
        return ApiResult.succ();
    }

    @RequestMapping(value = "statistics/customer", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 修复用户统计数据", notes = "修复用户统计数据")
    public ApiResult<NullObject> repairCustomerStatistics() {
        dataRepairV2_0_0.repairCustomerStatistics();
        return ApiResult.succ();
    }

    /*@RequestMapping(value = "prod/data", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 线上数据修复", notes = "线上数据修复")
    public ApiResult<SettlementCardWithdrawResponseVo> repairProdData() {

        SettlementCardWithdrawVo settlementCardWithdrawVo = new SettlementCardWithdrawVo();
        settlementCardWithdrawVo.setP1_bizType("SettlementCardWithdraw");
        settlementCardWithdrawVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlementCardWithdrawVo.setP3_userId("6099");
        String orderID = UUIDUnsigned32.randomUUIDString();
        System.out.println("orderID = " + orderID);
        settlementCardWithdrawVo.setP4_orderId(orderID);
        settlementCardWithdrawVo.setP5_amount("8912.69");
        settlementCardWithdrawVo.setP6_feeType("PAYER");

        return quickPayApi.settlementCardWithdraw(settlementCardWithdrawVo);
        return null;
    }*/

    /*@RequestMapping(value = "prod/data/query", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 线上数据修复查询", notes = "线上数据修复查询")
    public ApiResult<QuerySettlementCardVo> repairProdDataQuery(
            @RequestParam("orderId") String orderId) {
        QueryOrderVo queryOrderVo = new QueryOrderVo();
        queryOrderVo.setP1_bizType("TransferQuery");
        queryOrderVo.setP2_orderId(orderId);
        queryOrderVo.setP3_customerNumber(posConstants.getHelibaoMerchantNO());

        return quickPayApi.querySettlementCardWithdraw(queryOrderVo);
    }*/

    /*@RequestMapping(value = "prod/data/query/quickPay", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 线上数据修复查询", notes = "线上数据修复查询")
    public ApiResult<QueryOrderResponseVo> repairProdDataQuery(
            @RequestParam("orderId") String orderId) {
        QueryOrderVo queryOrderVo = new QueryOrderVo();
        queryOrderVo.setP1_bizType("QuickPayQuery");
        queryOrderVo.setP2_orderId(orderId);
        queryOrderVo.setP3_customerNumber(posConstants.getHelibaoMerchantNO());

        return quickPayApi.queryOrder(queryOrderVo);
    }*/

    /*public static void main(String[] args) {
        BigDecimal withdrawRate = new BigDecimal("0.0058");
        BigDecimal extraServiceCharge = new BigDecimal("3");
        BigDecimal helibaoPoundageRate = new BigDecimal("0.0038");
        BigDecimal helibaoTixianRate = new BigDecimal("0.0002");
        BigDecimal helibaoTixianMoney = new BigDecimal("1");

        // 获取订单金额
        BigDecimal orderAmount = new BigDecimal("8888");
        // 计算平台收取的服务费：serviceCharge = orderAmount * userWithdrawRate + extraServiceCharge
        BigDecimal serviceCharge = orderAmount
                .multiply(withdrawRate)
                .add(extraServiceCharge)
                .setScale(2, BigDecimal.ROUND_UP);
        System.out.println("-------------------------->总的服务费serviceCharge：" + serviceCharge);
        // 合利宝收取的手续费
        BigDecimal payCharge = orderAmount
                .multiply(helibaoPoundageRate)
                .setScale(2, BigDecimal.ROUND_UP);
        System.out.println("-------------------------->合利宝收取的手续费payCharge：" + payCharge);
        //提现的到账金额
        BigDecimal arrivalAmount = orderAmount.subtract(serviceCharge);
        System.out.println("-------------------------->提现的到账金额arrivalAmount：" + arrivalAmount);
        //公司支付给用户时 合利宝的手续费
        BigDecimal posCharge = arrivalAmount
                .multiply(helibaoTixianRate)
                .add(helibaoTixianMoney)
                .setScale(2, BigDecimal.ROUND_UP);
        System.out.println("-------------------------->公司支付给用户时，合利宝的手续费posCharge：" + posCharge);

        PosOutCardInfoDto outCard = new PosOutCardInfoDto();
        outCard.setCardNo("5707");
        outCard.setBankCode("SHB");
        outCard.setBankName("上海银行");
        outCard.setCardType((byte) 2);
        System.out.println("-------------------------->支出银行信息outCardInfo：" + JsonUtils.objectToJson(outCard));
    }*/

}
