/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.repair;

import com.google.common.collect.Lists;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.data.repair.v2_0_0.DataRepairV2_0_0;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.helipay.action.QuickPayApi;
import com.pos.transaction.helipay.vo.QueryOrderResponseVo;
import com.pos.transaction.helipay.vo.QueryOrderVo;
import com.pos.transaction.helipay.vo.QuerySettlementCardVo;
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

    @Resource
    private SmsService smsService;

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

    @RequestMapping(value = "clear/redis", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 清楚redis缓存", notes = "清楚redis缓存")
    public ApiResult<NullObject> clearRedis() {
        return dataRepairV2_0_0.clearRedisCache();
    }

    @RequestMapping(value = "statistics/daily/special", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 更新每日数据统计", notes = "更新每日数据统计")
    public ApiResult<NullObject> dailyDailyStatistics() {
        posStatisticsService.dailyStatisticsSchedule();
        return ApiResult.succ();
    }

    /*@RequestMapping(value = "prod/data", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 线上数据修复", notes = "线上数据修复")
    public ApiResult<SettlementCardWithdrawResponseVo> repairProdData() {

        SettlementCardWithdrawVo settlementCardWithdrawVo = new SettlementCardWithdrawVo();
        settlementCardWithdrawVo.setP1_bizType("SettlementCardWithdraw");
        settlementCardWithdrawVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlementCardWithdrawVo.setP3_userId("5088");
        String orderID = UUIDUnsigned32.randomUUIDString();
        System.out.println("orderID = " + orderID);
        settlementCardWithdrawVo.setP4_orderId(orderID);
        settlementCardWithdrawVo.setP5_amount("3132.82");
        settlementCardWithdrawVo.setP6_feeType("PAYER");

        return quickPayApi.settlementCardWithdraw(settlementCardWithdrawVo);
    }*/

    @RequestMapping(value = "prod/data/query", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 线上数据修复查询", notes = "线上数据修复查询")
    public ApiResult<QuerySettlementCardVo> repairTransferQuery(
            @RequestParam("orderId") String orderId) {
        // 2b0fc7f880b34f399e4b0fce30eee24e
        QueryOrderVo queryOrderVo = new QueryOrderVo();
        queryOrderVo.setP1_bizType("TransferQuery");
        queryOrderVo.setP2_orderId(orderId);
        queryOrderVo.setP3_customerNumber(posConstants.getHelibaoMerchantNO());

        return quickPayApi.querySettlementCardWithdraw(queryOrderVo);
    }

    @RequestMapping(value = "prod/data/query/quickPay", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 线上数据修复查询", notes = "线上数据修复查询")
    public ApiResult<QueryOrderResponseVo> repairQuickPayQuery(
            @RequestParam("orderId") String orderId) {
        QueryOrderVo queryOrderVo = new QueryOrderVo();
        queryOrderVo.setP1_bizType("QuickPayQuery");
        queryOrderVo.setP2_orderId(orderId);
        queryOrderVo.setP3_customerNumber(posConstants.getHelibaoMerchantNO());

        return quickPayApi.queryOrder(queryOrderVo);
    }

    @RequestMapping(value = "prod/spring-festival/notify", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 春节放假停运短信通知", notes = "春节放假停运短信通知")
    public ApiResult<List<String>> sendSpringFestivalNotify() {
        String phoneStr = "18581841473,18030701030,13880756056,18611195408,15002382675,15000168361,18181438321,18908069603,18696599440,18140090832,18030893331,13980620293,15213155146,13638326180,13638261311,13890399976,13594076253,13540761221,13880167618,18113173162,15680812222,13883740726,18982263588,13436134947,13508040031,13540498341,15928832523,18203098103,15902367847,15826161626,18725778907,13883739521,13883771446,15802362094,15928709768,13982105642,18680899560,13983924201,13399887784,18980539031,15008212139,13980757570,13880727515,13808086609,15308082018,18600056058,15694005968,18784994408,18128803854,18008022245,13032827168,13121511559,17628286583,15881260238,13608093402,18583381164,13908333305,15002857003,18382210425,18380460598,13540393484,15680626917";

        List<String> phoneNumbers = Lists.newArrayList(phoneStr.split(","));

        String message = "尊敬的钱刷刷用户，快捷收款功能将在春节期间暂停使用，暂停时间为2月11日-2月22日，由此给您带来的不便，我们深表歉意。";
        return smsService.sendMessageBatch(phoneNumbers, message);
    }

    public static void main(String[] args) {
        BigDecimal withdrawRate = new BigDecimal("0.0045");
        BigDecimal extraServiceCharge = new BigDecimal("3");
        BigDecimal helibaoPoundageRate = new BigDecimal("0.0040");
        BigDecimal helibaoTixianRate = new BigDecimal("0.0002");
        BigDecimal helibaoTixianMoney = new BigDecimal("1");

        // 获取订单金额
        BigDecimal orderAmount = new BigDecimal("9850");
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
        System.out.println("-------------------------->提现的到账金额：" + arrivalAmount);
        //公司支付给用户时 合利宝的手续费
        BigDecimal posCharge = arrivalAmount
                .multiply(helibaoTixianRate)
                .add(helibaoTixianMoney)
                .setScale(2, BigDecimal.ROUND_UP);
        System.out.println("-------------------------->公司支付给用户时，合利宝的手续费posCharge：" + posCharge);

        /*PosOutCardInfoDto outCard = new PosOutCardInfoDto();
        outCard.setCardNo("1558");
        outCard.setBankCode("CGB");
        outCard.setBankName("广发银行");
        outCard.setCardType((byte) 2);
        System.out.println("-------------------------->支出银行信息outCardInfo：" + JsonUtils.objectToJson(outCard));*/
    }

    /*@Resource
    private SecurityService securityService;

    @RequestMapping(value = "prod/bindCard/update", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 换绑银行卡", notes = "换绑银行卡")
    public ApiResult<SettlementCardBindResponseVo> updateBindCard() {

        Long userId = 5088L;
        String encryptedName = "QG7Q9Hs8S/elubA2bZ9wNS2geaasS7VoISjGZaqsJ5WMreVUxbuosYnVSHL0XVgGiVhc05d0mVpfn830EaSDOVC/SDlrkBroPqh0+PPecJPBDDG9KPofDEc0JcWe8CFFjQbDJPezZLG8aB5pfq/vOOJJjdOG28DYivIGV35UFqY=";
        String encryptedIdCardNo = "apz+aGDz/vZpo0dooIEsid+D7bG7ifal2EwJK4ulEnypU6JWSyarB2Ve1Rqy6JvcDpzRHOPqE/BGOikMphfj09C69fhEvRprsVZEKa6ILxsYRLDo4e9qqImQFe9nOv05FnqOSNEtGBjv0XBik85/mJ1o+8uGUj8Rs6mYEPcAMYg=";
        String encryptedCardNo = "Jy/vfVfBkyw365YYrwXpteU950cVN8mzDow5pxmPVotu7o/iQ0S8za4asMTHvXEKwNB7j9uHpYxhSbOp1kquPVQ4cbbF9b1SILH2mB9mw5bHXVSH0FV1V/qKgu35CleDoqhFxSwgrERGjdV85pnfpPSyD27supi4CqwQRKNjvc0=";
        String encryptedPhone = "LJbMZ6KHUXMxGO91WNwj7O0SYmefpLFuQWZ50c5SlGzCJ9z/IyLDYpedXNM4S2Vka8ziWjW0xTOLxDXQv4n7Y5GKWtTWXoPzpvaagkpt4K4tVU6wqWHGt9YHxQe4Pg/vvhLXrammY6w3W8AEMn1wgoFaInOKXkw6gEw0SRQRykw=";


        //解密之后的数据 调用接口时需要  数据库存储的是加密了的数据
        String decryptName = securityService.decryptData(encryptedName);
        String decryptCardNo = securityService.decryptData(encryptedCardNo);
        String decryptIdCardNo = securityService.decryptData(encryptedIdCardNo);
        String decryptPhone = securityService.decryptData(encryptedPhone);
        String orderId = UUID.randomUUID().toString();
        SettlementCardBindVo settlementCardBindVo = new SettlementCardBindVo();
        settlementCardBindVo.setP1_bizType("SettlementCardBind");
        settlementCardBindVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlementCardBindVo.setP3_userId(String.valueOf(userId));
        settlementCardBindVo.setP4_orderId(orderId);
        settlementCardBindVo.setP5_payerName(decryptName);
        settlementCardBindVo.setP6_idCardType("IDCARD");
        settlementCardBindVo.setP7_idCardNo(decryptIdCardNo);
        settlementCardBindVo.setP8_cardNo(decryptCardNo);
        settlementCardBindVo.setP9_phone(decryptPhone);
        settlementCardBindVo.setP11_operateType("UPDATE");

        return quickPayApi.settlementCardBind(settlementCardBindVo);
    }*/

}
