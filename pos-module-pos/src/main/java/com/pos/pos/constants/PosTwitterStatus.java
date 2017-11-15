/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.constants;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * POS 推客状态枚举
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
public enum PosTwitterStatus implements CommonIntEnum {

    DISABLE(1, "未启用"),

    ENABLE(2, "已启用"),

    CLOSED(3,"已关闭");

    /**
     * 标识码
     */
    private final int code;

    /**
     * 描述内容
     */
    private final String desc;

    PosTwitterStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PosTwitterStatus getEnum(int code) {
        for (PosTwitterStatus type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的推客状态类型code值！");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int getCode() {
        return code;
    }
}
