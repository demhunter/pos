/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.v1_0_0;

/**
 * 清洗POS交易数据
 * <p>
 *     1、清洗交易数据的付款银行卡信息，主要作用为解密SQL变更的付款银行卡信息数据
 * </p>
 *
 * @author wangbing
 * @version 1.0, 2017/10/25
 */
public class RepairPosDataV_1_0_0 {

    /*private final static Logger logger = LoggerFactory.getLogger(RepairPosDataV_1_0_0.class);

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private SecurityService securityService;

    public ApiResult<NullObject> repairPosTransactionOutCardInfo() {
        logger.info("------------------------->开始修复快捷收款交易记录的付款银行卡数据");
        List<UserPosTransactionRecord> records = posUserTransactionRecordDao.queryTransactionRecord(null, null, LimitHelper.create(1, Integer.MAX_VALUE, false));
        if (CollectionUtils.isEmpty(records)) {
            logger.info("------------------------->当前没有需要修复的数据");
        } else {
            int successCount = 0; // 累计成功个数
            logger.info("------------------------->当前共有{}个需要修复的数据", records.size());
            for (UserPosTransactionRecord record : records) {
                if (StringUtils.isNotEmpty(record.getOutCardInfo())) {
                    PosOutCardInfoDto outCardInfo = JsonUtils.jsonToObject(record.getOutCardInfo(), new TypeReference<PosOutCardInfoDto>() {
                    });
                    String cardNo = securityService.decryptData(outCardInfo.getCardNo());
                    outCardInfo.setCardNo(cardNo.substring(cardNo.length() - 4, cardNo.length()));
                    record.setOutCardInfo(JsonUtils.objectToJson(outCardInfo));
                    posUserTransactionRecordDao.updateTransactionOutCardInfo(record);
                    successCount++;
                }
            }
            logger.info("------------------------->成功修复{}个交易记录数据", successCount);
        }
        logger.info("------------------------->修复快捷收款交易记录的付款银行卡数据结束");
        return ApiResult.succ();
    }*/
}
