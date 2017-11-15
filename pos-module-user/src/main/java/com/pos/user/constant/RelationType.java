package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

/**
 *客户与家居顾问关联的渠道
 * Created by 睿智 on 2017/7/4.
 */
public enum RelationType implements CommonByteEnum {
    CREATE_SESSION((byte) 1, "创建会话"),

    CREATE_ORDER((byte) 2, "E端创建订单"),

    TWITTER_REPORT((byte) 3, "推客报备"),

    SCAN((byte) 4, "C端扫描"),

    REQUEST_HELP((byte) 5, "我要帮助"),

    PAY_SUPERVISOR_DEPOSIT((byte)6,"支付监理订单定金");

    private final byte code;

    private final String desc;

    RelationType(final byte code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RelationType getEnum(byte code) {
        for (RelationType type : RelationType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
