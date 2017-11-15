/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.pos.common.util.validation.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 反射Utils
 *
 * @author cc
 * @version 1.0, 2017/3/6
 */
public class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 检查对象是否字段都为空
     *
     * @param obj 对象
     * @param excludeFields 排除字段
     * @return 检查结果
     */
    public static Boolean checkAllFieldValueNull(Object obj, List<String> excludeFields) {
        Preconditions.checkArgsNotNull(obj);

        Boolean whetherAllNull = true;

        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (!CollectionUtils.isEmpty(excludeFields) && excludeFields.contains(f.getName())) {
                    continue;
                }

                if (f.get(obj) != null) {
                    whetherAllNull = false;
                    break;
                }
            } catch (IllegalAccessException e) {
                logger.error("检查对象字段时有反射异常！", e);
                throw new IllegalStateException(e.getMessage());
            }
        }

        return whetherAllNull;
    }

}
