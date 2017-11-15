/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.support;

import com.google.common.base.Strings;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 排序参数助手类
 *
 * Created by cc on 16/6/7.
 */
public class OrderHelper implements Serializable {

    private static final long serialVersionUID = -2217211008540541826L;

    private String fieldName;

    private String ordination = "DESC";

    public OrderHelper(String fieldName) {
        if (Strings.isNullOrEmpty(fieldName)) {
            throw new IllegalArgumentException("`filedName` is not empty!");
        }
        this.fieldName = fieldName;
    }

    public OrderHelper(String fieldName, boolean isAsc) {
        this(fieldName);
        if (isAsc) {
            this.ordination = "ASC";
        }
    }

    public OrderHelper(String fieldName, String ordination) {
        this(fieldName);
        if ("ASC".equalsIgnoreCase(ordination) || "DESC".equalsIgnoreCase(ordination)) {
            this.ordination = ordination;
        } else {
            throw new IllegalArgumentException("`ordination`: ASC / DESC");
        }
    }

    /**
     * 验证是否允许使用当前字段/别名来排序.
     *
     * @param orderField 允许排序的字段/别名定义
     * @throws ValidationException 不允许使用当前字段/别名来排序
     */
    public void validate(OrderField orderField) {
        if (!orderField.check(fieldName)) {
            throw new ValidationException("不允许使用指定字段/别名来排序: " + fieldName
                    + ", 可选值: " + Arrays.toString(orderField.getNames().toArray()));
        }
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOrdination() {
        return ordination;
    }

    public void setOrdination(String ordination) {
        this.ordination = ordination;
    }

}
