/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 平台数据统计排序类型
 *
 * @author wangbing
 * @version 1.0, 2017/03/27
 */
public enum StatisticsOrderbyType implements CommonByteEnum{

    CASE_COUNT_DESC((byte) 1, "按作品数量降序排序");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    StatisticsOrderbyType(byte code, String desc) {
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

    public static StatisticsOrderbyType getEnum(byte code) {
        for (StatisticsOrderbyType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法平台数据统计排序类型code值");
    }
}
