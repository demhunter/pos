/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 平台数据统计-时间搜索类型
 *
 * @author wangbing
 * @version 1.0, 2017/03/28
 */
public enum StatisticsTimeSearchType implements CommonByteEnum {

    SERACH_DAY((byte) 1, "按日"),

    SEARCH_WEEK((byte) 2, "按周"),

    SEARCH_MONTH((byte) 3, "按月");


    StatisticsTimeSearchType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final byte code;

    private final String desc;

    public String getDesc() {
        return desc;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public static StatisticsTimeSearchType getEnum(byte code) {

        for (StatisticsTimeSearchType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的时间搜索类型code值");
    }
}
