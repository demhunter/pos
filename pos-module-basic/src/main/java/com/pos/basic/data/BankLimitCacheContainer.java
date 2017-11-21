/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import com.google.common.collect.Maps;
import com.pos.basic.dao.BankLimitDao;
import com.pos.basic.dto.bank.LimitInfoDto;
import com.pos.common.util.validation.Preconditions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 银行限额CacheContainer
 *
 * @author cc
 * @version 1.0, 2017/1/9
 */
public class BankLimitCacheContainer {

    @Resource
    private BankLimitDao bankLimitDao;

    private Map<String, Object> limitMap;

    @PostConstruct
    public void initialize() {
        limitMap = Maps.newHashMap();

        List<Map<String, Object>> resultList = bankLimitDao.queryBankLimits();
        for (Map<String, Object> entryMap : resultList) {
            limitMap.put(entryMap.get("bankName") + ":" + entryMap.get("cardType"), new LimitInfoDto((BigDecimal) entryMap.get("limitPerOp"), (BigDecimal) entryMap.get("limitPerDay"), (BigDecimal) entryMap.get("limitPerMonth")));
        }
    }

    /**
     * 根据银行名称和卡类型查限额信息
     *
     * @param bankName 银行名称
     * @param cardType 卡类型，1表示借记卡，2表示信用卡
     * @return 限额信息
     */
    public LimitInfoDto queryLimitInfo(String bankName, Byte cardType) {
        Preconditions.checkArgsNotNull(bankName, cardType);

        Object object = limitMap.get(bankName + ":" + cardType);
        if (object == null) {
            return null;
        }

        return (LimitInfoDto) object;
    }
}
