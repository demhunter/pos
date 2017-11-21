/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.statistics;

import com.pos.basic.constant.PhoneSystemType;
import com.pos.basic.constant.StatisticsOrderbyType;
import com.pos.basic.constant.StatisticsTimeRangeType;
import com.pos.basic.constant.StatisticsTimeSearchType;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.CommonEnumDto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 平台统计相关枚举定义
 *
 * @author wangbing
 * @version 1.0, 2017/03/27
 */
public class StatisticsEnumDto implements Serializable {

    /**
     * {@link PhoneSystemType}
     */
    @ApiModelProperty("手机系统类型定义")
    private List<CommonEnumDto> phoneSystemTypes;

    /**
     * {@link StatisticsOrderbyType}
     */
    @ApiModelProperty("数据统计排序类型定义")
    private List<CommonEnumDto> orderbyTypes;

    /**
     * {@link StatisticsTimeRangeType}
     */
    @ApiModelProperty("数据统计时间段类型定义")
    private List<CommonEnumDto> timeRangeTypes;

    /**
     * {@link StatisticsTimeSearchType}
     */
    @ApiModelProperty("数据统计时间搜索类型定义")
    private List<CommonEnumDto> timeSearchTypes;

    private static final StatisticsEnumDto instance = new StatisticsEnumDto();

    private StatisticsEnumDto() {
        phoneSystemTypes = Stream.of(PhoneSystemType.values())
                .map(e -> new CommonEnumDto(e.getCode(), e.getDesc())).collect(Collectors.toList());
        orderbyTypes = Stream.of(StatisticsOrderbyType.values())
                .map(e -> new CommonEnumDto(e.getCode(), e.getDesc())).collect(Collectors.toList());
        timeRangeTypes = Stream.of(StatisticsTimeRangeType.values())
                .map(e -> new CommonEnumDto(e.getCode(), e.getDesc())).collect(Collectors.toList());
        timeSearchTypes = Stream.of(StatisticsTimeSearchType.values())
                .map(e -> new CommonEnumDto(e.getCode(), e.getDesc())).collect(Collectors.toList());
    }

    public static StatisticsEnumDto getInstance() {
        return instance;
    }

    public List<CommonEnumDto> getPhoneSystemTypes() {
        return phoneSystemTypes;
    }

    public List<CommonEnumDto> getOrderbyTypes() {
        return orderbyTypes;
    }

    public List<CommonEnumDto> getTimeRangeTypes() {
        return timeRangeTypes;
    }

    public List<CommonEnumDto> getTimeSearchTypes() {
        return timeSearchTypes;
    }
}
