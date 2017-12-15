/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.version;

import com.google.common.base.Strings;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 版本号信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public class VersionDto implements Serializable {
    private static final long serialVersionUID = 1467697428422163901L;
    private static final byte VERSION_LENGTH = 3; // 可比较的版本号的固定分段长度: xx.xx.xx

    @ApiModelProperty("第一版本号")
    private Integer firstVersion;

    @ApiModelProperty("第二版本号")
    private Integer secondVersion;

    @ApiModelProperty("第三版本号")
    private Integer thirdVersion;

    public VersionDto() {
    }

    public VersionDto(Integer firstVersion, Integer secondVersion, Integer thirdVersion) {
        this.firstVersion = firstVersion;
        this.secondVersion = secondVersion;
        this.thirdVersion = thirdVersion;
    }

    public static VersionDto create(Integer[] versionArray) {
        return new VersionDto(versionArray[0], versionArray[1], versionArray[2]);
    }

    /**
     * 根据传入版本号字符串解析Version版本号
     *
     * @param versionStr 点分三位版本号字符串（xx.xx.xx，xx[0-99]）
     * @return 版本信息
     */
    public static VersionDto parse(String versionStr) {
        if (Strings.isNullOrEmpty(versionStr)) {
            throw new ValidationException("版本号不能为空！");
        }
        String[] versionStrArr = versionStr.split("\\.");
        if (versionStrArr.length != VERSION_LENGTH) {
            throw new ValidationException("版本号格式错误：" + versionStr);
        }
        Integer[] versionIntArr = new Integer[versionStrArr.length];
        try {
            for (byte i = 0; i < versionStrArr.length; i++) {
                versionIntArr[i] = Integer.parseInt(versionStrArr[i]);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("版本号类型错误：" + versionStr);
        }

        return VersionDto.create(versionIntArr);
    }

    /**
     * 与传入版本相比较
     *
     * @param version 被比较版本
     * @param ignoreLast 是否忽略最后一位（第三位）版本号
     * @return 0 = 版本号相同；-1 = 小于被比较版本；1 = 大于被比较版本
     */
    public int compare(VersionDto version, boolean ignoreLast) {
        if (version == null) {
            return 0;
        }

        // 比较第一位版本号
        int firstCompare = this.firstVersion.compareTo(version.getFirstVersion());
        if (firstCompare != 0) {
            return firstCompare;
        }
        // 比较第二位版本号
        int secondCompare = this.secondVersion.compareTo(version.getSecondVersion());
        if (secondCompare != 0 || ignoreLast) {
            return secondCompare;
        }
        // 比较第三位版本号
        return this.thirdVersion.compareTo(version.getThirdVersion());
    }

    /**
     * 必填字段校验
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(firstVersion, fieldPrefix + "firstVersion");
        FieldChecker.checkEmpty(secondVersion, fieldPrefix + "secondVersion");
        FieldChecker.checkEmpty(thirdVersion, fieldPrefix + "thirdVersion");
    }

    @Override
    public String toString() {
        String point = ".";
        return firstVersion + point + secondVersion + point + thirdVersion;
    }

    public Integer getFirstVersion() {
        return firstVersion;
    }

    public void setFirstVersion(Integer firstVersion) {
        this.firstVersion = firstVersion;
    }

    public Integer getSecondVersion() {
        return secondVersion;
    }

    public void setSecondVersion(Integer secondVersion) {
        this.secondVersion = secondVersion;
    }

    public Integer getThirdVersion() {
        return thirdVersion;
    }

    public void setThirdVersion(Integer thirdVersion) {
        this.thirdVersion = thirdVersion;
    }
}
