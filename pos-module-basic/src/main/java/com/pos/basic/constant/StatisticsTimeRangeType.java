/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 平台数据统计时间段类型
 *
 * @author wangbing
 * @version 1.0, 2017/03/27
 */
public enum StatisticsTimeRangeType implements CommonByteEnum {

    USER_DEFINED((byte) 1, "自定义"),

    LATEST_WEEK((byte) 2, "过去7天"),

    LATEST_MONTH((byte) 3, "过去30天"),

    LATEST_TWO_MONTH((byte) 4, "过去60天");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    StatisticsTimeRangeType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static StatisticsTimeRangeType getEnum(byte code) {
        for (StatisticsTimeRangeType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法平台数据统计时间段类型code值");
    }
}
