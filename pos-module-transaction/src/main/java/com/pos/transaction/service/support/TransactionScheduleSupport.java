/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support;

import com.pos.basic.constant.RedisConstants;
import com.pos.basic.sm.fsm.FSM;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.dao.PosUserTransactionRecordDao;
import com.pos.transaction.domain.UserPosTransactionRecord;
import com.pos.transaction.fsm.PosFSMFactory;
import com.pos.transaction.fsm.context.TransactionStatusTransferContext;
import com.pos.transaction.helipay.action.QuickPayApi;
import com.pos.transaction.helipay.vo.QueryOrderVo;
import com.pos.transaction.helipay.vo.QuerySettlementCardVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 交易定时任务支持类
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
@Component
public class TransactionScheduleSupport {

    private static final Logger log = LoggerFactory.getLogger(TransactionScheduleSupport.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PosConstants posConstants;

    @Resource
    private QuickPayApi quickPayApi;

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    public void queryTransactionStatus() {
        long queueSize = redisTemplate.opsForList().size(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE);
        if (queueSize > 0) {
            log.info("-------------------->当前共有{}个交易待查询", queueSize);
            // 已当前队列数据，启动轮询
            for (long index = 0; index < queueSize; index++) {
                Long recordId = Long.valueOf(redisTemplate.opsForList().leftPop(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE));
                UserPosTransactionRecord transactionRecord = posUserTransactionRecordDao.get(recordId);
                TransactionStatusType statusType = TransactionStatusType.getEnum(transactionRecord.getStatus());
                // 只轮询状态处于处理中的交易
                if (statusType.canPolling()) {
                    QueryOrderVo queryOrderVo = new QueryOrderVo();
                    queryOrderVo.setP1_bizType("TransferQuery");
                    queryOrderVo.setP2_orderId(transactionRecord.getRecordNum());
                    queryOrderVo.setP3_customerNumber(posConstants.getHelibaoMerchantNO());
                    ApiResult<QuerySettlementCardVo> result = quickPayApi.querySettlementCardWithdraw(queryOrderVo);
                    TransactionStatusTransferContext context = new TransactionStatusTransferContext();
                    context.setRecordId(recordId);
                    if (result.isSucc()) {
                        QuerySettlementCardVo data = result.getData();
                        log.info("-------------------->交易{}查询结果{}", recordId, JsonUtils.objectToJson(data));
                        // 查询成功
                        context.setSerialNumber(data.getRt6_serialNumber());
                        FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
                        // 合利宝总共有6中状态，依次为：
                        // RECEIVE  已接单（PS：为新加状态）
                        // INIT     初始化状态
                        // DOING    处理中
                        // SUCCESS  成功
                        // FAIL     失败
                        // REFUND   退款
                        if ("SUCCESS".equals(data.getRt7_orderStatus())) {
                            // 交易成功
                            fsm.processFSM("withdrawSuccess");
                        } else if ("RECEIVE".equals(data.getRt7_orderStatus())
                                || "INIT".equals(data.getRt7_orderStatus())
                                || "DOING".equals(data.getRt7_orderStatus())) {
                            // 交易处理中，重新入队列，待下次查询
                            redisTemplate.opsForList().rightPush(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE, recordId.toString());
                        } else {
                            // 交易失败
                            fsm.processFSM("withdrawFailed");
                        }
                    } else {
                        FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
                        log.error("查询交易id={}的信息失败，CodeError={}，msg={}",
                                transactionRecord.getId(), result.getError(), result.getMessage());
                        fsm.processFSM("withdrawFailed");
                    }
                }
            }
        } else {
            log.info("-------------------->当前没有待处理的交易");
        }
    }
}
