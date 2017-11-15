/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

/**
 * 易宝支付钱脉产品银行信息读取和入库，仅支持本地读取，不作为公共方法和服务
 *
 * @author cc
 * @version 1.0, 2016/10/25
 */
public class BandDataImportTool {

    public static void main(String[] args) {
        XSSFWorkbook workbook = null;
        InputStream inputStream = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://192.168.28.16:3306/ywmj??useUnicode=true&amp;zeroDateTimeBehavior=convertToNull&amp;useServerPrepStmts=false", "ywmj", "ywmj16530");
            Statement stmt = con.createStatement();

            /* 文件从本地读取，不可复用 */
            inputStream = new FileInputStream("/Users/cc/Desktop/支付/支付+钱脉系统技术接入服务手册及资料包-版权所有/附件2-支付+钱脉所有银行支行省市库表.xlsx");
            workbook = new XSSFWorkbook(inputStream);
            inputStream.close();

            Sheet sheet = workbook.getSheetAt(0);
            /* 不取第一行的title */
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                /* 分别为支行全称，联行号，总行名称，省名称，市名称 */
                String[] cellStrs = new String[5];

                Row row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    /* 严格要求数据的一致性 */
                    Assert.assertTrue(row.getLastCellNum() == 5);

                    Cell cell = row.getCell(j);
                    cellStrs[j] = "\'" + cell.getStringCellValue() + "\'";
                }

                String sql = "insert into bank(branch_name, `number`, head_name, province_name, city_name) "
                        + "values (" + cellStrs[0] + ", " + cellStrs[1] + ", " + cellStrs[2] + ", " + cellStrs[3] + ", " + cellStrs[4] + ")";
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
