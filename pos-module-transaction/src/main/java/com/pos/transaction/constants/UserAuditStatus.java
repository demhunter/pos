/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.constants;

import com.pos.basic.enumHandler.CommonIntEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * 用户身份认证状态类型枚举
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
public enum UserAuditStatus implements CommonIntEnum {

    NOT_SUBMIT(0, "未提交"),

    NOT_AUDIT(1, "未审核"),

    AUDITED(2, "已通过"),

    REJECTED(3, "未通过");

    private final int code;

    private final String desc;

    UserAuditStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserAuditStatus getEnum(int code) {
        for (UserAuditStatus type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalParamException("非法的用户身份认证状态类型code值！");
    }

    public static UserAuditStatus getEnum(String symbol) {
        for (UserAuditStatus type : values()) {
            if (Objects.equals(type.toString(), symbol)) {
                return type;
            }
        }

        throw new IllegalParamException("非法的用户身份认证状态类型symbol值！");
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int getCode() {
        return code;
    }
}
