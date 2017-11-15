/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.view;

/**
 * 属性值的格式化设置.
 *
 * @author wayne
 * @version 1.0, 2017/3/22
 */
public class ValueFormat {

    static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static final String DEFAULT_TRUE_FORMAT = "是";

    static final String DEFAULT_FALSE_FORMAT = "否";

    private String dateFormat;

    private String trueFormat;

    private String falseFormat;

    public static ValueFormat getDefaultInstance() {
        ValueFormat instance = new ValueFormat();
        instance.setDateFormat(DEFAULT_DATE_FORMAT);
        instance.setTrueFormat(DEFAULT_TRUE_FORMAT);
        instance.setFalseFormat(DEFAULT_FALSE_FORMAT);
        return instance;
    }

    public String formatBoolean(Boolean value) {
        return Boolean.TRUE.equals(value) ? trueFormat : falseFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTrueFormat() {
        return trueFormat;
    }

    public void setTrueFormat(String trueFormat) {
        this.trueFormat = trueFormat;
    }

    public String getFalseFormat() {
        return falseFormat;
    }

    public void setFalseFormat(String falseFormat) {
        this.falseFormat = falseFormat;
    }

}