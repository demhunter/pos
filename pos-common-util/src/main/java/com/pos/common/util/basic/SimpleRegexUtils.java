/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;
import org.apache.commons.lang.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的正则表达式工具类.
 *
 * @author wayne
 * @version 1.0, 2016/7/8
 */
public class SimpleRegexUtils {

    /**
     * 判断指定字符串是否是手机号码.
     *
     * @param mobile 手机号码
     * @return
     */
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 判断是否是座机号码
     * @param telephone
     * @return
     */
    public static boolean isTelephone(String telephone){
        Pattern p = Pattern.compile("^(0[0-9]{2,3}\\-)?([1-9][0-9]{6,7})$");
        Matcher m = p.matcher(telephone);
        return m.matches();
    }

    /**
     * 隐藏手机号的中间4位, 如151****1381.
     *
     * @param mobile 手机号码
     * @return 隐藏后的手机号码
     */
    public static String hiddenMobile(String mobile) {
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /**
     * 隐藏姓名的中间字符，如：张三 -> 张*；王老五 -> 王*五
     * PS：姓名为空或只有一个字，则不做处理直接返回
     *
     * @param name 姓名
     * @return 隐藏后的姓名
     */
    public static String hiddenName(String name) {
        if (StringUtils.isEmpty(name) || name.length() == 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)([\\u4e00-\\u9fa5]{1})", "$1" + "*");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < name.length() - 2; i++) {
            builder.append("*");
        }
        String asteriskStr = builder.toString();
        return name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)([\\u4e00-\\u9fa5]{1})", "$1" + asteriskStr + "$3");
    }

    /**
     * 隐藏银行卡号除开始4位和末尾4位的中间所有数字，如6217 **** **** 9660
     *
     * @param cardNumber 银行卡号码
     * @return 隐藏后的银行卡号码
     */
    public static String hiddenCardNumber(String cardNumber) {
        return cardNumber.replaceAll("(\\d{4})\\d*(\\d{4})", "$1 **** **** $2");
    }

    /**
     * 检查指定字符串是否为合法的身份证号(ps: 只支持第2代身份证).
     *
     * @param idNumber 身份证号
     * @throws IllegalParamException 身份证格式错误
     */
    public static void checkIdNumber(String idNumber) {
        Pattern p = Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X)$");
        Matcher m = p.matcher(idNumber);
        if (!m.matches()) {
            throw new IllegalParamException("身份证号格式不正确：" + idNumber);
        }
    }

    /**
     * 从身份证号中提取其性别(ps: 只支持第2代身份证).
     *
     * @param idNumber 身份证号
     * @param check    是否检查身份证号的合法性, 如果为false请确保格式正确, 否则有可能抛出未知异常
     * @return 0 = 女性, 1 = 男性(多么形象)
     * @throws IllegalParamException check=true且身份证格式错误
     */
    public static byte getGenderByIdNumber(String idNumber, boolean check) {
        if (check) {
            checkIdNumber(idNumber);
        }
        int code = Integer.parseInt(idNumber.substring(14, 17));
        return code % 2 == 0 ? (byte) 0 : (byte) 1; // 按2代身份证规则, 顺序码为偶数表示女性, 奇数表示男性
    }

    /**
     * 从身份证号中提取其出生日期(ps: 只支持第2代身份证).
     *
     * @param idNumber 身份证号
     * @param check    是否检查身份证号的合法性, 如果为false请确保格式正确, 否则有可能抛出未知异常
     * @return yyyyMMdd
     * @throws IllegalParamException check=true且身份证格式错误
     */
    public static Date getBirthDateByIdNumber(String idNumber, boolean check) {
        if (check) {
            checkIdNumber(idNumber);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(idNumber.substring(6, 10)));
        cal.set(Calendar.MONTH, Integer.parseInt(idNumber.substring(10, 12)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(idNumber.substring(12, 14)));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 从身份证号中提取其年龄(ps: 只支持第2代身份证).
     *
     * @param idNumber 身份证号
     * @param check    是否检查身份证号的合法性, 如果为false请确保格式正确, 否则有可能抛出未知异常
     * @return 如果出生日期在当前日期之后返回0
     * @throws IllegalParamException check=true且身份证格式错误
     */
    public static int getAgeByIdNumber(String idNumber, boolean check) {
        Date birthDate = getBirthDateByIdNumber(idNumber, check);
        Date nowTime = new Date();
        return nowTime.after(birthDate) ? SimpleDateUtils.yearsOfDuration(birthDate, nowTime) : 0;
    }

    /**
     * 校验公司营业执行号码是否合法
     *
     * @param bizLicence 公司营业执照号码
     */
    public static void checkBizLicence(String bizLicence) {
        FieldChecker.checkEmpty(bizLicence, "营业执照号码");
        boolean checkResult;
        if (bizLicence.length() == 15) {
            checkResult = StringUtils.isNumeric(bizLicence);
        } else if (bizLicence.length() == 18){
            String regex = "[0-9A-Z]{2}[0-9]{6}[0-9A-Z]{10}";
            checkResult = Pattern.compile(regex).matcher(bizLicence).matches();
        } else {
            throw new IllegalParamException("营业执照号码长度不合法");
        }

        if (!checkResult) {
            throw new IllegalParamException("请输入符合规范的营业执照号码");
        }
    }

}