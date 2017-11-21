/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.support;

import com.pos.basic.service.SecurityService;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.pos.dao.PosUserTransactionRecordDao;
import com.pos.pos.domain.UserPosTransactionRecord;
import com.pos.pos.dto.PosOutCardInfoDto;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 清洗POS交易数据
 * <p>
 *     1、清洗交易数据的付款银行卡信息，主要作用为解密SQL变更的付款银行卡信息数据
 * </p>
 *
 * @author wangbing
 * @version 1.0, 2017/10/25
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class RepairPosData {

    private final static Logger logger = LoggerFactory.getLogger(RepairPosData.class);

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
    }
}
