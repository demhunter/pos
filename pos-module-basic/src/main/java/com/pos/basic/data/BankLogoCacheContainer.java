/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import com.google.common.collect.Maps;
import com.pos.basic.dao.BankLogoDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 银行logo数据Container
 *
 * @author cc
 * @version 1.0, 2017/1/6
 */
@Component
public class BankLogoCacheContainer {

    @Resource
    private BankLogoDao bankLogoDao;

    private Map<String, String> logoMap;

    @PostConstruct
    public void initialize() {
        logoMap = Maps.newHashMap();

        List<Map<String, String>> resultList = bankLogoDao.queryBankLogos();
        for (Map<String, String> entryMap : resultList) {
            logoMap.put(entryMap.get("bankName"), entryMap.get("logo"));
        }
    }

    /**
     * 根据银行名获取银行logo URL，如数据集中没有，则返回银联logo
     *
     * @param bankName 银行名
     * @return logo URL
     */
    public String queryLogoByBankName(String bankName) {
        if (StringUtils.isEmpty(bankName)) {
            return bankName;
        }

        String logo = logoMap.get(bankName);
        if (logo == null) {
            return logoMap.get("中国银联股份有限公司");
        }

        return logo;
    }
}
