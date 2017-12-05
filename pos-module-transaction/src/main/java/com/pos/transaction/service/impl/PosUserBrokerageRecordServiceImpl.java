/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.constants.GetAgentEnum;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.dao.PosTwitterBrokerageDao;
import com.pos.transaction.dao.PosUserBrokerageRecordDao;
import com.pos.transaction.dao.PosUserChannelDao;
import com.pos.transaction.dao.PosUserTransactionRecordDao;
import com.pos.transaction.domain.UserPosChannelInfo;
import com.pos.transaction.dto.PosUserGetBrokerageRecordDto;
import com.pos.transaction.exception.PosUserErrorCode;
import com.pos.transaction.service.PosUserBrokerageRecordService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 快捷受收款用户佣金ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosUserBrokerageRecordServiceImpl implements PosUserBrokerageRecordService {

    private final static Logger LOG = LoggerFactory.getLogger(PosUserBrokerageRecordServiceImpl.class);

    @Resource
    private PosUserChannelDao posUserChannelDao;

    @Resource
    private PosUserBrokerageRecordDao posUserBrokerageRecordDao;

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private PosTwitterBrokerageDao posTwitterBrokerageDao;

    @Resource
    private SmsService smsService;

    @Resource
    private CustomerService customerService;

    @Resource
    private PosConstants posConstants;

    @Override
    public ApiResult<NullObject> saveBrokerageRecord(PosUserGetBrokerageRecordDto record, UserIdentifier user) {
        FieldChecker.checkEmpty(record, "record");
        FieldChecker.checkEmpty(user, "user");
        record.check("record");

        UserPosChannelInfo channel = posUserChannelDao.get(record.getUserId());
        if (channel == null) {
            return ApiResult.fail(PosUserErrorCode.CHANNEL_NOT_EXISTED);
        }
        if (!channel.getApplyMoney().equals(record.getAmount())) {
            LOG.error("data apply money:{}; handled apply money:{}", channel.getApplyMoney(), record.getAmount());
            return ApiResult.fail(PosUserErrorCode.CHANNEL_APPLY_MONEY_ERROR);
        }
        // 保存处理记录
        posUserBrokerageRecordDao.save(record);
        // 累计提现总金额，清空当前提现申请金额
        BigDecimal totalMoney = channel.getTotalMoney() == null ? BigDecimal.ZERO : channel.getTotalMoney();
        channel.setTotalMoney(totalMoney.add(record.getAmount()));
        channel.setApplyMoney(BigDecimal.ZERO);
        posUserChannelDao.update(channel);
        // 标记已提现的收款
        posTwitterBrokerageDao.markAgentStatus(record.getUserId(),
                GetAgentEnum.APPLY.getCode(), GetAgentEnum.GET.getCode(), null);
        posTwitterBrokerageDao.markParentAgentStatus(record.getUserId(),
                GetAgentEnum.APPLY.getCode(), GetAgentEnum.GET.getCode(), null);
        // 发送申请已处理短信
        CustomerDto customer = customerService.findById(record.getUserId(), true, true);
        if (customer != null) {
            String message = String.format(posConstants.getPosTwitterBrokerageHandledTemplate(), record.getAmount());
            smsService.sendMessage(customer.getUserPhone(), message);
        }
        return ApiResult.succ();
    }


    @Override
    public List<PosUserGetBrokerageRecordDto> queryBrokerageRecord(UserIdentifier user, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(user, "user");

        return posUserBrokerageRecordDao.queryRecords(user, limitHelper);
    }
}
