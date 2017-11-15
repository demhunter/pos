package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

/**
 * Created by 睿智 on 2017/7/3.
 */
public enum InvitationChannel implements CommonByteEnum {

    SHARE((byte)1,"分享"),INVITATIONCODE((byte)2,"邀请码");

    private final byte code;

    private final String desc;

    InvitationChannel(byte code, String desc) {
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


    public static InvitationChannel getEnum(byte code) {
        for (InvitationChannel type : InvitationChannel.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值");
    }

    public static InvitationChannel getEnum(String desc) {
        for (InvitationChannel type : InvitationChannel.values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }

}
