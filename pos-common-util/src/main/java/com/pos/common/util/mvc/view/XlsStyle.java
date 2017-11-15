/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.view;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * XLS样式设置.
 *
 * @author wayne
 * @version 1.0, 2017/3/22
 */
public class XlsStyle {

    static final short DEFAULT_COLUMN_WIDTH = 15;

    private String sheetName;

    private int columnWidth;

    public static XlsStyle getDefaultInstance() {
        XlsStyle instance = new XlsStyle();
        instance.setColumnWidth(DEFAULT_COLUMN_WIDTH);
        return instance;
    }

    void fillSheetStyles(Sheet sheet) {
        if (columnWidth > 0) {
            sheet.setDefaultColumnWidth(columnWidth);
        } else {
            sheet.setDefaultColumnWidth(XlsStyle.DEFAULT_COLUMN_WIDTH);
        }
    }

    public String getSheetName() {
        return sheetName;
    }

    public XlsStyle setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public XlsStyle setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
        return this;
    }

}