/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.sms.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

import java.util.Objects;

/**
 * 验证码使用类型
 *
 * Created by cc on 16/6/8.
 */
public enum MemcachedPrefixType implements CommonByteEnum {

    REGISTER((byte) 1, "用户注册", "reg:"),

    RETRIEVE((byte) 2, "找回密码", "ret:"),

    LOGIN((byte) 3, "登录验证", "log:"),

    EMPLOYEE((byte) 4, "生成业者帐号", "em:"),// 目前只针对业者

    CONFIRM_CUSTOMER((byte) 5, "确认开通客户角色", "con_cu:"),

    FIRST_LOGIN_TOKEN((byte) 6, "首次登录token", "fl_to:"),

    H5_LOGIN_TOKEN((byte) 7, "登录token（原生与H5）", "h5_login:"),

    COMPANY_APPLY((byte) 8, "公司入驻申请", "b_join:"),

    TWITTER_CUSTOMER_REPORT((byte) 9, "推客客户报备", "tw_report"),

    TWITTER_INVITATION((byte) 10, "邀请成为推客", "tw_invitation"),

    MATERIAL_JOIN((byte)11, "邀请客户享受主材补贴", "ma_invitation");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    /**
     * Cache中对应key的前缀
     * 完整形式如：reg:13412345678
     */
    private final String prefix;

    MemcachedPrefixType(byte code, String desc, String prefix) {
        this.code = code;
        this.desc = desc;
        this.prefix = prefix;
    }

    public static MemcachedPrefixType getEnum(byte code) {
        for (MemcachedPrefixType codeType : values()) {
            if (Objects.equals(codeType.code, code)) {
                return codeType;
            }
        }

        return null;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrefix() {
        return prefix;
    }
}
