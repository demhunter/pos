/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.constants;

import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * @author 睿智
 * @version 1.0, 2017/8/23
 */
public enum BankCodeNameEnum  {
    ICBC("ICBC", "工商银行"),
    ABC("ABC", "农业银行"),
    BOC("BOC","中国银行"),
    CCB("CCB","建设银行"),
    CMBCHINA("CMBCHINA","招商银行"),
    POST("POST","邮政储蓄"),
    ECITIC("ECITIC","中信银行"),
    CEB("CEB","光大银行"),
    BOCO("BOCO","交通银行"),
    CIB("CIB","兴业银行"),
    CMBC("CMBC","民生银行"),
    PINGAN("PINGAN","平安银行"),
    CGB("CGB","广发银行"),
    BCCB("BCCB","北京银行"),
    HXB("HXB","华夏银行"),
    SPDB("SPDB","浦发银行"),
    SHB("SHB","上海银行");

    /**
     * 标识码
     */
    private final String code;

    /**
     * 描述内容
     */
    private final String desc;

    BankCodeNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BankCodeNameEnum getEnum(String code) {
        for (BankCodeNameEnum bankCodeName : values()) {
            if (Objects.equals(bankCodeName.code, code)) {
                return bankCodeName;
            }
        }

        throw new IllegalParamException("非法的银行代码code值");
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
