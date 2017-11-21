/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pos.basic.dao.PayAreaDao;
import com.pos.basic.domain.PayArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * PayAreaCacheContainer
 *
 * @author cc
 * @version 1.0, 2016/12/1
 */
public class PayAreaCacheContainer {

    private static final Logger logger = LoggerFactory.getLogger(PayAreaCacheContainer.class);

    @Resource
    private PayAreaDao payAreaDao;

    private Map<String, List<String>> areas;

    @PostConstruct
    public void initialize() {
        areas = Maps.newHashMap();
        List<PayArea> payAreas = payAreaDao.queryPayAreas();

        for (PayArea payArea : payAreas) {
            if (areas.containsKey(payArea.getProvinceName().trim())) {
                areas.get(payArea.getProvinceName().trim()).add(payArea.getCityName().trim());
            } else {
                areas.put(payArea.getProvinceName().trim(), Lists.newArrayList(payArea.getCityName().trim()));
            }
        }
    }

    public List<String> queryProvinces() {
        return Lists.newArrayList(areas.keySet());
    }

    public List<String> queryCitiesByProvince(String province) {
        return areas.get(province);
    }
}
