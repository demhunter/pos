/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

import java.util.Objects;

/**
 * 城市类型
 *
 * @author cc
 * @version 1.0, 2017/5/4
 */
public enum CityType implements CommonByteEnum {

    ALL((byte) 0, "全部", 0),

    CHENGDU((byte) 1, "成都", 2294),

    CHONGQING((byte) 2, "重庆", 2252);

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 城市名
     */
    private final String desc;

    /**
     * 城市id
     */
    private final long id;

    CityType(byte code, String desc, long id) {
        this.code = code;
        this.desc = desc;
        this.id = id;
    }

    public static CityType getEnum(byte code) {
        for (CityType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的code值！");
    }

    public static boolean contains(byte code) {
        for (CityType type : values()) {
            if (Objects.equals(type.code, code)) {
                return true;
            }
        }

        return false;
    }

    public long getId() {
        return id;
    }

    @Override
    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
