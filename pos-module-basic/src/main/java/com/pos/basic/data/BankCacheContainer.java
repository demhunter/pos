/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import com.google.common.collect.Lists;
import com.pos.basic.dao.BankDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 银行缓存容器
 *
 * @author cc
 * @version 1.0, 2016/12/1
 */
@Component
public class BankCacheContainer {

    private static final Logger logger = LoggerFactory.getLogger(BankCacheContainer.class);

    @Resource
    private BankDao bankDao;

    private List<String> banks;

    @PostConstruct
    public void initialize() {
        banks = bankDao.queryHeadNames();
    }

    /**
     * 模糊查询银行名称
     *
     * @param key 查询关键字
     * @return 名称列表
     */
    public List<String> queryBanksByKey(String key) {
        List<String> fitBanks = Lists.newArrayList();

        for (String bank : banks) {
            if (bank.contains(key)) {
                fitBanks.add(bank);
            }
        }

        if (CollectionUtils.isEmpty(fitBanks)) {
            return null;
        }
        return fitBanks;
    }
}
