/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

/**
 * 用户细分类型接口.
 *
 * @author wayne
 * @version 1.0, 2016/8/5
 */
public interface UserDetailType {

    /**
     * 获取类型值.
     */
    byte getValue();

    /**
     * 获取类型描述.
     */
    String getDesc();

    /**
     * 获取类型枚举名称.
     */
    String getName();

    /**
     * 比较指定值是否与当前类型值相同.
     *
     * @param value 指定类型值
     */
    boolean compare(byte value);

}
