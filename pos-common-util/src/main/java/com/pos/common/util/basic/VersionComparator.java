/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.google.common.base.Strings;
import com.pos.common.util.exception.ValidationException;

/**
 * 版本号比较器.
 *
 * @author wayne
 * @version 1.0, 2016/8/16
 */
public class VersionComparator {

    /**
     * 可比较的版本号的固定分段长度: xx.xx.xx
     */
    public static final byte VERSION_LENGTH = 3;

    /**
     * 比较指定的两个版本号, 版本号格式: xx.xx.xx, 其中xx必须是0或者任意正整数.
     * <p>
     * 当ignoreLast = true时, 表示最后一位将作为修复版本号而不进行比较, 比如: compare(1.5.0, 1.5.1, true) return 0
     * </p>
     *
     * @param version1   要比较的第一个版本号
     * @param version2   要比较的第二个版本号
     * @param ignoreLast 是否忽略比较最后一位
     * @return version1小于version2返回-1; version1大于version2返回1; 版本号相等返回0
     * @throws ValidationException 任意一个版本号解析失败
     */
    public static int compare(String version1, String version2, boolean ignoreLast) {
        int[] version1Arr = parseVersion(version1);
        int[] version2Arr = parseVersion(version2);

        byte len = ignoreLast ? VERSION_LENGTH - 1 : VERSION_LENGTH;
        for (byte i = 0; i < len; i++) {
            if (version1Arr[i] > version2Arr[i]) {
                return 1;
            } else if (version1Arr[i] < version2Arr[i]) {
                return -1;
            }
        }
        return 0;
    }

    private static int[] parseVersion(String version) {
        if (Strings.isNullOrEmpty(version)) {
            throw new ValidationException("版本号不能为空！");
        }
        String[] versionStrArr = version.split("\\.");
        if (versionStrArr == null || versionStrArr.length != VERSION_LENGTH) {
            throw new ValidationException("版本号格式错误：" + version);
        }
        int[] versionIntArr = new int[versionStrArr.length];
        try {
            for (byte i = 0; i < versionStrArr.length; i++) {
                versionIntArr[i] = Integer.parseInt(versionStrArr[i]);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("版本号类型错误：" + version);
        }
        return versionIntArr;
    }

}