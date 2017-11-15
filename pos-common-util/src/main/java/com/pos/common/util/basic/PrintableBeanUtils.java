/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * toString工具类
 *
 * Created by cc on 16/6/7.
 */
public class PrintableBeanUtils {

    private PrintableBeanUtils() {
    }

    public static String toString(Object object) {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}