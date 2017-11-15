/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;

import java.util.Objects;

/**
 * 直辖市枚举
 *
 * @author lifei
 * @version 1.0, 2017/8/21
 */
public enum CharteredCitiesType implements CommonByteEnum {

    BEIJING((byte) 1, "北京", 1),

    TIANJIN((byte) 2, "天津", 20),

    SHANGHAI((byte) 3, "上海", 797),

    CHONGQING((byte) 4, "重庆", 2252);

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

    CharteredCitiesType(byte code, String desc, long id) {
        this.code = code;
        this.desc = desc;
        this.id = id;
    }

    public static CharteredCitiesType getEnum(byte code) {
        for (CharteredCitiesType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的code值！");
    }

    public static boolean contains(byte code) {
        for (CharteredCitiesType type : values()) {
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
