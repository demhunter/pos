/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.validation;

import com.google.common.base.Strings;
import com.pos.common.util.exception.RequiredParamException;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.basic.SimpleStringUtils;
import com.pos.common.util.exception.IllegalParamException;

/**
 * 常用字段的验证工具类.
 * <p>
 * 与FieldChecker的区别：Validator是常用字段的逻辑校验，与当前业务场景相关，而FieldChecker提供任意字段的通用校验方法.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class Validator {

    public static final int NAME_MIN_LENGTH = 2;

    public static final int NAME_MAX_LENGTH = 10;

    public static final int NICK_NAME_MIN_LENGTH = 1;

    public static final int NICK_NAME_MAX_LENGTH = 15;

    public static final int USER_NAME_MIN_LENGTH = 8;

    public static final int USER_NAME_MAX_LENGTH = 20;

    public static final int PASSWORD_MIN_LENGTH = 8;

    public static final int PASSWORD_MAX_LENGTH = 20;

    public static void checkUserName(String userName) {
        if (Strings.isNullOrEmpty(userName)) {
            throw new RequiredParamException("用户名不能为空");
        }

        if (userName.length() < USER_NAME_MIN_LENGTH
                || userName.length() > USER_NAME_MAX_LENGTH) {
            throw new IllegalParamException("用户名长度必须在"
                    + USER_NAME_MIN_LENGTH + "-" + USER_NAME_MAX_LENGTH + "位以内");
        }

        if (!SimpleStringUtils.isNumberOrLetter(userName)) {
            throw new IllegalParamException("用户名只能由数字(0-9)或者大小写字母(a-z,A-Z)组成");
        }
    }

    public static void checkPassword(String password) {
        if (Strings.isNullOrEmpty(password)) {
            throw new RequiredParamException("密码不能为空");
        }

        if (password.length() < PASSWORD_MIN_LENGTH
                || password.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalParamException("密码长度必须在"
                    + PASSWORD_MIN_LENGTH + "-" + PASSWORD_MAX_LENGTH + "位以内");
        }

        if (!SimpleStringUtils.isNumberOrLetter(password)) {
            throw new IllegalParamException("密码只能由数字(0-9)或者大小写字母(a-z,A-Z)组成");
        }
    }

    public static void checkCnName(String cnName) {
        if (Strings.isNullOrEmpty(cnName)) {
            throw new RequiredParamException("姓名不能为空");
        }

        if (cnName.length() < NAME_MIN_LENGTH
                || cnName.length() > NAME_MAX_LENGTH) {
            throw new IllegalParamException("姓名长度必须在"
                    + NAME_MIN_LENGTH + "-" + NAME_MAX_LENGTH + "位以内");
        }

        if (!SimpleStringUtils.isChinese(cnName)) {
            throw new IllegalParamException("姓名只能输入中文");
        }
    }

    public static void checkNickName(String nickName) {
        if (Strings.isNullOrEmpty(nickName)) {
            throw new RequiredParamException("昵称不能为空");
        }

        if (nickName.length() < NICK_NAME_MIN_LENGTH
                || nickName.length() > NICK_NAME_MAX_LENGTH) {
            throw new IllegalParamException("昵称长度必须在"
                    + NICK_NAME_MIN_LENGTH + "-" + NICK_NAME_MAX_LENGTH + "位以内");
        }

        if (SimpleStringUtils.containsEmoji(nickName)) {
            throw new IllegalParamException("昵称不能包含表情字符");
        }
    }

    public static void checkMobileNumber(String mobile) {
        if (Strings.isNullOrEmpty(mobile)) {
            throw new RequiredParamException("手机号不能为空");
        }

        if (mobile.length() != 11) {
            throw new IllegalParamException("手机号长度不正确：" + mobile);
        }

        if (!SimpleRegexUtils.isMobile(mobile)) {
            throw new IllegalParamException("手机号格式不正确：" + mobile);
        }
    }

    public static void checkIdNumber(String idNumber) {
        if (Strings.isNullOrEmpty(idNumber)) {
            throw new RequiredParamException("身份证号不能为空");
        }

        if (idNumber.length() != 18) { // 只支持第2代身份证
            throw new IllegalParamException("身份证号长度不正确：" + idNumber);
        }
        SimpleRegexUtils.checkIdNumber(idNumber);
    }

    public static void main(String[] args) {

    }

}