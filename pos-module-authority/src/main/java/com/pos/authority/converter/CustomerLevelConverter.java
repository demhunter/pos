/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.converter;

import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.authority.dto.level.CustomerLevelConfigDto;
import com.pos.authority.dto.level.CustomerUpgradeLevelDto;
import org.springframework.beans.BeanUtils;

/**
 * 客户等级Converter
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
public class CustomerLevelConverter {

    public static CustomerLevelConfigDto toCustomerLevelConfigDto(CustomerLevelConfig config) {
        CustomerLevelConfigDto dto = new CustomerLevelConfigDto();

        BeanUtils.copyProperties(config, dto);

        return dto;
    }

    public static CustomerUpgradeLevelDto toCustomerUpgradeLevelDto() {
        CustomerUpgradeLevelDto dto = new CustomerUpgradeLevelDto();

        return dto;
    }
}
