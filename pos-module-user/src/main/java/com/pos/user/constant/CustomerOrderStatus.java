package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 家居顾问的客户订单的状态
 * Created by 睿智 on 2017/7/3.
 */
public enum CustomerOrderStatus implements CommonByteEnum {

    TALKORDER((byte) 1, "谈单中"),

    COMPLETEORDER((byte) 2, "已成单"),

    REFUSEORDER((byte) 3, "已飞单"),

    FINISHORDER((byte) 4, "已完结");

    private final byte code;

    private final String desc;

    CustomerOrderStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public byte getValue() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CustomerOrderStatus getEnum(byte code) {
        for (CustomerOrderStatus type : CustomerOrderStatus.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值");
    }

    public static CustomerOrderStatus getEnum(String desc) {
        for (CustomerOrderStatus type : CustomerOrderStatus.values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }

    public static Boolean talkOrRefuse(Byte status) {
        if (TALKORDER.equals(getEnum(status)) || REFUSEORDER.equals(getEnum(status))) {
            return true;
        }
        return false;
    }

    public static Boolean completeOrFinish(Byte status) {
        if (COMPLETEORDER.equals(getEnum(status)) || FINISHORDER.equals(getEnum(status))) {
            return true;
        }
        return false;
    }
}
