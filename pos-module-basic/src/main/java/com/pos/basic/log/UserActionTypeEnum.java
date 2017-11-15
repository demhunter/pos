/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.log;

import com.pos.basic.enumHandler.CommonIntEnum;

import java.util.Objects;

/**
 * 用户行为类型枚举类
 * @author 睿智
 * @version 1.0, 2017/10/18
 */
public enum UserActionTypeEnum implements CommonIntEnum {

    ACTION_TYPE_APP(1, "APP相关"),

    ACTION_TYPE_ACCOUNT(2, "账号相关"),

    ACTION_TYPE_CASE(3, "作品相关"),

    ACTION_TYPE_DISCOUNT(4, "优惠相关"),

    ACTION_TYPE_GUARANTEE(5, "保障相关"),

    ACTION_TYPE_CHAT(6, "聊天相关"),

    ACTION_TYPE_MY(7, "我的相关");

    /**
     * 标识码
     */
    private final int code;

    /**
     * 描述内容
     */
    private final String desc;

    UserActionTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserActionTypeEnum getEnum(int code) {
        for (UserActionTypeEnum type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的用户行为类型枚举类型code值！");
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}