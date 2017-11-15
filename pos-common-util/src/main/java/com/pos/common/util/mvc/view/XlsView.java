/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.view;

import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * XLS视图生成器.
 *
 * @author wayne
 * @version 1.0, 2017/3/22
 */
public class XlsView extends AbstractXlsView {

    private String[] columnNames; // XLS第一行的列名

    private List<Object[]> cellValues; // List中的数组表示每行的列值，且顺序与columnNames相匹配

    private XlsStyle xlsStyle; // 设置XLS样式，如表格名，列宽度等等

    private ValueFormat valueFormat; // 根据属性类型设置列值的格式化显示

    private SimpleDateFormat dateFormat;

    public XlsView(int initialCapacity, String[] columnNames) {
        if (columnNames == null || columnNames.length == 0) {
            throw new NullPointerException("未设置XLS列名（columnNames）！");
        }
        this.columnNames = columnNames;
        this.cellValues = new ArrayList<>(initialCapacity > 0 ? initialCapacity : 0);
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        buildBefore();
        Sheet sheet = createSheet(workbook);
        writeColumnNames(sheet); // 写入列名（第一行）
        writeCellValues(sheet); // 写入每行每列的数据
        buildAfter(response);
    }

    /**
     * 向XLS添加一行中的列值.
     *
     * @param values 列值数组, 其个数必须与列名相同(columnNames)
     */
    public void addRowValues(Object[] values) {
        if (values == null || values.length == 0) {
            throw new NullPointerException("XLS每行的列值不能为空！");
        }
        if (values.length != columnNames.length) {
            throw new IllegalArgumentException("XLS每行的列值必须与列名个数相匹配！");
        }
        cellValues.add(values);
    }

    public XlsView setXlsStyle(XlsStyle xlsStyle) {
        this.xlsStyle = xlsStyle;
        return this;
    }

    public XlsView setValueFormat(ValueFormat valueFormat) {
        this.valueFormat = valueFormat;
        return this;
    }

    private void buildBefore() {
        if (xlsStyle == null) {
            xlsStyle = XlsStyle.getDefaultInstance();
        }
        if (valueFormat == null) {
            valueFormat = ValueFormat.getDefaultInstance();
        }
        dateFormat = new SimpleDateFormat(valueFormat.getDateFormat());
    }

    private void buildAfter(HttpServletResponse response) {
        String filename = System.currentTimeMillis() + ".xls";
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
    }

    private Sheet createSheet(Workbook workbook) {
        Sheet sheet = !Strings.isNullOrEmpty(xlsStyle.getSheetName())
                ? workbook.createSheet(xlsStyle.getSheetName()) : workbook.createSheet();
        xlsStyle.fillSheetStyles(sheet);
        return sheet;
    }

    private void writeColumnNames(Sheet sheet) {
        Row firstRow = sheet.createRow(0);
        for (short i = 0; i < columnNames.length; i++) {
            firstRow.createCell(i).setCellValue(columnNames[i]);
        }
    }

    private void writeCellValues(Sheet sheet) {
        if (!cellValues.isEmpty()) {
            for (int i = 0; i < cellValues.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                Object[] values = cellValues.get(i);
                for (short j = 0; j < values.length; j++) {
                    dataRow.createCell(j).setCellValue(formatValue(values[j]));
                }
            }
        }
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Boolean) {
            return valueFormat.formatBoolean((Boolean) value);
        } else if (value instanceof Date) {
            return dateFormat.format((Date) value);
        } else {
            return value.toString();
        }
    }

}