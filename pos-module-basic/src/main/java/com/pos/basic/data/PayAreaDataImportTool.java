/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 易宝支付钱脉省市编码
 *
 * @author cc
 * @version 1.0, 2016/10/25
 */
public class PayAreaDataImportTool {

    public static void main(String[] args) {
        HSSFWorkbook workbook = null;
        InputStream inputStream = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.28.16:3306/ywmj??useUnicode=true&amp;zeroDateTimeBehavior=convertToNull&amp;useServerPrepStmts=false", "ywmj", "ywmj16530");
            Statement stmt = con.createStatement();

            /* 文件从本地读取，不可复用 */
            inputStream = new FileInputStream("/Users/cc/Desktop/支付/支付+钱脉系统技术接入服务手册及资料包-版权所有/附件1-支付+钱脉易宝省市编号表.xls");
            workbook = new HSSFWorkbook(inputStream);
            inputStream.close();

            Sheet sheet = workbook.getSheetAt(0);
            /* 不取第一行的title */
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                /* 分别为省，省编码，市，市编码 */
                String[] cellStrs = new String[4];

                Row row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    /* 严格要求数据的一致性 */
                    Assert.assertTrue(row.getLastCellNum() == 4);

                    Cell cell = row.getCell(j);
                    cellStrs[j] = "\'" + (j % 2 == 0 ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue()).substring(0, String.valueOf(cell.getNumericCellValue()).indexOf('.'))) + "\'";
                }

                String sql = "insert into pay_area(province_name, province_code, city_name, city_code) "
                        + "values (" + cellStrs[0] + ", " + cellStrs[1] + ", " + cellStrs[2] + ", " + cellStrs[3] + ")";
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
