/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.google.common.collect.Lists;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.condition.orderby.PosTransactionOrderField;
import com.pos.pos.condition.query.PosTransactionCondition;
import com.pos.pos.converter.PosConverter;
import com.pos.pos.dao.PosCardDao;
import com.pos.pos.dao.PosUserTransactionRecordDao;
import com.pos.pos.domain.PosTransaction;
import com.pos.pos.dto.card.PosCardDto;
import com.pos.pos.dto.twitter.TwitterDailyStatisticsDto;
import com.pos.pos.dto.transaction.TransactionRecordDto;
import com.pos.pos.service.PosUserTransactionRecordService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pos交易ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosUserTransactionRecordServiceImpl implements PosUserTransactionRecordService {

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private PosCardDao posCardDao;

    @Resource
    private CustomerService customerService;

    @Resource
    private SecurityService securityService;

    @Override
    public List<TwitterDailyStatisticsDto> queryDailyStatistics(UserIdentifier user, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(user, "user");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        return posUserTransactionRecordDao.queryDailyStatistics(user, limitHelper);
    }

    @Override
    public Integer queryUserTransactionCount(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");
        PosTransactionCondition condition = new PosTransactionCondition();
        condition.setUserId(userId);

        return posUserTransactionRecordDao.queryTransactionRecordCount(condition);
    }

    @Override
    public ApiResult<Pagination<TransactionRecordDto>> queryUserTransactionRecord(PosTransactionCondition condition, OrderHelper orderHelper, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(PosTransactionOrderField.getInstance());
        }
        Integer totalCount = posUserTransactionRecordDao.queryTransactionRecordCount(condition);
        Pagination<TransactionRecordDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<PosTransaction> transactions = posUserTransactionRecordDao.queryTransactionRecord(condition, orderHelper, limitHelper);
            if (!CollectionUtils.isEmpty(transactions)) {
                Set<Long> inCardIds = new HashSet<>();
                Set<Long> userIds = new HashSet<>();
                List<TransactionRecordDto> result = transactions.stream().map(transaction -> {
                    TransactionRecordDto recordDto = PosConverter.toTransactionRecordDto(transaction);
                    inCardIds.add(transaction.getInCardId());
                    userIds.add(transaction.getUserId());
                    return recordDto;
                }).collect(Collectors.toList());
                List<PosCardDto> cards = posCardDao.queryPosCardByIds(Lists.newArrayList(inCardIds));
                Map<Long, PosCardDto> cardMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(cards)) {
                    cards.forEach(e -> cardMap.put(e.getId(), decryptPosCardInfo(e)));
                }
                List<CustomerDto> customers = customerService.findInUserIds(Lists.newArrayList(userIds), true, true);
                Map<Long, CustomerDto> userMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(customers)) {
                    customers.forEach(e -> userMap.put(e.getId(), e));
                }
                result.forEach(e -> {
                    e.setInCardInfo(cardMap.get(e.getInCardId()));
                    CustomerDto customer = userMap.get(e.getUserId());
                    if (customer != null) {
                        e.setUserName(customer.getRealName());
                        e.setUserPhone(customer.getPhone());
                    }
                });
                pagination.setResult(result);
            }

        }

        return ApiResult.succ(pagination);
    }

    /**
     * 解密银行卡信息<br/>
     * 此解密返回一个新的对象，需要调用者接收并保存，原对象不变
     *
     * @param source 需要被解密的银行卡
     * @return 解密后的银行卡信息
     */
    @SuppressWarnings("all")
    private PosCardDto decryptPosCardInfo(PosCardDto source) {
        PosCardDto target = source.copy();

        target.setBankName(securityService.decryptData(target.getBankName()));
        target.setIdCardNo(securityService.decryptData(target.getIdCardNo()));
        target.setBankCardNo(securityService.decryptData(target.getBankCardNo()));
        target.setMobilePhone(securityService.decryptData(target.getMobilePhone()));

        return target;
    }
}
