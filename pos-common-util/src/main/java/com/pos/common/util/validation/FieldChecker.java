/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.validation;

import com.pos.common.util.exception.RequiredParamException;
import com.pos.common.util.exception.IllegalParamException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字段值或类型的检查工具类.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class FieldChecker {

    @SuppressWarnings("all")
    public static void checkEmpty(Object value, String fieldName) {
        if (value == null) {
            throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
        }
        if (value instanceof String) {
            if (((String) value).trim().length() == 0) {
                throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
            }
        } else if (value instanceof Collection<?>) {
            if (((Collection<?>) value).size() == 0) {
                throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
            }
        } else if (value instanceof Map<?, ?>) {
            if (((Map<?, ?>) value).size() == 0) {
                throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
            }
        }
    }

    public static void checkMinValue(int value, int minValue, String fieldName) {
        if (value < minValue) {
            throw new IllegalParamException("'" + fieldName + "'" + "必须大于等于" + minValue);
        }
    }

    public static void checkMaxValue(int value, int maxValue, String fieldName) {
        if (value > maxValue) {
            throw new IllegalParamException("'" + fieldName + "'" + "必须小于等于" + maxValue);
        }
    }

    public static void checkMinMaxValue(int value, int minValue, int maxValue,
                                       String fieldName) {
        if (value < minValue || value > maxValue) {
            throw new IllegalParamException(
                    "'" + fieldName + "'" + "必须大于等于" + minValue + "，且小于等于" + maxValue);
        }
    }

    /**
     * [minValue, maxValue]
     */
    public static void checkMinMaxValue(BigDecimal value, BigDecimal minValue, BigDecimal maxValue,
                                        String fieldName) {
        checkEmpty(value, fieldName);
        checkEmpty(minValue, fieldName);
        checkEmpty(maxValue, fieldName);
        if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
            throw new IllegalParamException(
                    "'" + fieldName + "'" + "必须大于等于" + minValue + "，且小于等于" + maxValue);
        }
    }

    /**
     * [minValue, maxValue]
     */
    public static void checkEqualValue(BigDecimal value, BigDecimal thisValue,
                                        String fieldName) {
        checkEmpty(value, fieldName);
        checkEmpty(thisValue, fieldName);
        if (value.compareTo(thisValue) < 0 || value.compareTo(thisValue) > 0) {
            throw new IllegalParamException(
                    fieldName + "固定为" + thisValue + "%");
        }
    }

    /**
     * (minValue, maxValue]
     */
    public static void checkMinMaxValue2(BigDecimal value, BigDecimal minValue, BigDecimal maxValue,
                                        String fieldName) {
        checkEmpty(value, fieldName);
        checkEmpty(minValue, fieldName);
        checkEmpty(maxValue, fieldName);
        if (value.compareTo(minValue) <= 0 || value.compareTo(maxValue) > 0) {
            throw new IllegalParamException(
                    "'" + fieldName + "'" + "必须大于" + minValue + "，且小于等于" + maxValue);
        }
    }

    /**
     * (minValue, maxValue)
     */
    public static void checkMinMaxValue3(BigDecimal value, BigDecimal minValue, BigDecimal maxValue,
                                         String fieldName) {
        checkEmpty(value, fieldName);
        checkEmpty(minValue, fieldName);
        checkEmpty(maxValue, fieldName);
        if (value.compareTo(minValue) <= 0 || value.compareTo(maxValue) >= 0) {
            throw new IllegalParamException(
                    "'" + fieldName + "'" + "必须大于" + minValue + "，且小于" + maxValue);
        }
    }

    public static void checkMinValue(BigDecimal value, BigDecimal minValue, String fieldName) {
        checkEmpty(value, fieldName);
        checkEmpty(minValue, fieldName);
        if (value.compareTo(minValue) < 0 ) {
            throw new IllegalParamException(
                    "'" + fieldName + "'" + "必须大于等于" + minValue );
        }
    }

    public static void checkMinLength(String value, int minLength,
                                      String fieldName) {
        checkEmpty(value, fieldName);
        if (value.length() < minLength) {
            throw new IllegalParamException("'" + fieldName + "'" + "的长度必须大于等于" + minLength);
        }
    }

    public static void checkMaxLength(String value, int maxLength,
                                      String fieldName) {
        checkEmpty(value, fieldName);
        if (value.length() > maxLength) {
            throw new IllegalParamException("'" + fieldName + "'" + "的长度必须小于等于" + maxLength);
        }
    }

    public static void checkMinMaxLength(String value, int minLength,
                                        int maxLength, String fieldName) {
        checkEmpty(value, fieldName);
        if (value.length() < minLength || value.length() > maxLength) {
            throw new IllegalParamException(
                    "'" + fieldName + "'" + "的长度必须大于等于" + minLength + "，且小于等于" + maxLength);
        }
    }

    public static void checkMinMaxSize(List<String> values, int minSize,
                                       int maxSize, String fieldName, String unitDesc) {
        checkEmpty(values, fieldName);
        if (values.size() < minSize || values.size() > maxSize) {
            throw new IllegalParamException(
                    "'" + fieldName + "'的" + unitDesc + "必须大于等于" + minSize + "，且小于等于" + maxSize);
        }
    }

    public static void checkMaxSize(List<String> values, int maxSize,
                                      String fieldName, String unitDesc) {
        checkEmpty(values, fieldName);
        if (values.size() > maxSize) {
            throw new IllegalParamException("'" + fieldName + "'的" + unitDesc +"必须小于等于" + maxSize);
        }
    }

}