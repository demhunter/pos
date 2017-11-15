/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.constants.GetAgentEnum;
import com.pos.pos.constants.PosConstants;
import com.pos.pos.dao.PosUserChannelDao;
import com.pos.pos.dao.TwitterBrokerageDao;
import com.pos.pos.dao.TwitterBrokerageHandledDao;
import com.pos.pos.domain.Twitter;
import com.pos.pos.dto.BrokerageHandledRecordDto;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.service.PosUserBrokerageRecordService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
    private TwitterBrokerageHandledDao twitterBrokerageHandledDao;

    @Resource
    private TwitterBrokerageDao twitterBrokerageDao;

    @Resource
    private SmsService smsService;

    @Resource
    private CustomerService customerService;

    @Resource
    private PosConstants posConstants;

    @Override
    public ApiResult<NullObject> saveBrokerageRecord(BrokerageHandledRecordDto record, UserIdentifier user) {
        FieldChecker.checkEmpty(record, "record");
        FieldChecker.checkEmpty(user, "user");
        record.check("record");

        Twitter channel = posUserChannelDao.get(record.getUserId());
        if (channel == null) {
            return ApiResult.fail(PosUserErrorCode.CHANNEL_NOT_EXISTED);
        }
        if (!channel.getApplyMoney().equals(record.getAmount())) {
            LOG.error("data apply money:{}; handled apply money:{}", channel.getApplyMoney(), record.getAmount());
            return ApiResult.fail(PosUserErrorCode.CHANNEL_APPLY_MONEY_ERROR);
        }
        // 保存处理记录
        twitterBrokerageHandledDao.save(record);
        // 累计提现总金额，清空当前提现申请金额
        BigDecimal totalMoney = channel.getTotalMoney() == null ? BigDecimal.ZERO : channel.getTotalMoney();
        channel.setTotalMoney(totalMoney.add(record.getAmount()));
        channel.setApplyMoney(BigDecimal.ZERO);
        posUserChannelDao.update(channel);
        // 标记已提现的收款
        twitterBrokerageDao.markTwitterStatus(channel.getUserId(),
                GetAgentEnum.APPLY.getCode(), GetAgentEnum.GET.getCode(), null);
        twitterBrokerageDao.markParentStatus(user.getUserId(),
                GetAgentEnum.NOT_GET.getCode(), GetAgentEnum.APPLY.getCode(), null);
        // 发送申请已处理短信
        CustomerDto customer = customerService.findById(record.getUserId(), true);
        if (customer != null) {
            String message = String.format(posConstants.getPosTwitterBrokerageHandledTemplate(), record.getAmount());
            smsService.sendMessage(customer.getPhone(), message);
        }
        return ApiResult.succ();
    }
}
