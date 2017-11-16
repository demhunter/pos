/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.condition.orderby.PosUserOrderField;
import com.pos.pos.condition.query.CustomerCondition;
import com.pos.pos.dao.AuthorityDao;
import com.pos.pos.dto.user.PosUserIntegrateDto;
import com.pos.pos.constants.PosConstants;
import com.pos.pos.dao.PosUserTransactionRecordDao;
import com.pos.pos.dto.transaction.TransactionSimpleStatisticsDto;
import com.pos.pos.service.PosCustomerService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 快捷收款用户Service
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosCustomerServiceImpl implements PosCustomerService {

    @Resource
    private CustomerService customerService;

    @Resource
    private SecurityService securityService;

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private PosConstants posConstants;

    @Override
    public Pagination<PosUserIntegrateDto> queryPosCustomers(CustomerCondition condition, OrderHelper orderHelper, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(PosUserOrderField.getInstance());
        }

        int totalCount = authorityDao.getPosUserCount(condition);
        Pagination<PosUserIntegrateDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<PosUserIntegrateDto> result = authorityDao.queryPosUsers(condition, orderHelper, limitHelper);
            if (!CollectionUtils.isEmpty(result)) {
                List<Long> userIds = result.stream().map(PosUserIntegrateDto::getUserId).collect(Collectors.toList());
                Map<Long, CustomerDto> customerMap = customerService.getCustomerDtoMapById(userIds);
                List<TransactionSimpleStatisticsDto> statisticsList = posUserTransactionRecordDao.querySimpleStatistics(userIds);
                Map<Long, TransactionSimpleStatisticsDto> statisticsMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(statisticsList)) {
                    statisticsList.forEach(e -> statisticsMap.put(e.getUserId(), e));
                }
                result.forEach(e -> {
                    e.setPoundage(posConstants.getPosExtraPoundage());
                    e.setPosUserInfo(customerMap.get(e.getUserId()));
                    TransactionSimpleStatisticsDto statistics = statisticsMap.get(e.getUserId());
                    if (statistics == null) {
                        e.setUserPosCount(0);
                        e.setUserPosAmount(BigDecimal.ZERO);
                    } else {
                        e.setUserPosCount(statistics.getTransactionCount());
                        e.setUserPosAmount(statistics.getTransactionAmount());
                    }
                    decryptPosCardInfo(e);
                });
            }
            pagination.setResult(result);
        }

        return pagination;
    }

    /**
     * 解密银行卡信息<br/>
     * 此解密返回一个新的对象，需要调用者接收并保存，原对象不变
     *
     * @param source 需要被解密的银行卡
     * @return 解密后的银行卡信息
     */
    private void decryptPosCardInfo(PosUserIntegrateDto source) {
        if (StringUtils.isNotEmpty(source.getBankCardName())) {
            source.setBankCardName(securityService.decryptData(source.getBankCardName()));
        }
        if (StringUtils.isNotEmpty(source.getBankCardIdCardNO())) {
            source.setBankCardIdCardNO(securityService.decryptData(source.getBankCardIdCardNO()));
        }
        if (StringUtils.isNotEmpty(source.getBankCardNO())) {
            source.setBankCardNO(securityService.decryptData(source.getBankCardNO()));
        }
        if (StringUtils.isNotEmpty(source.getMobilePhone())) {
            source.setMobilePhone(securityService.decryptData(source.getMobilePhone()));
        }
    }
}
