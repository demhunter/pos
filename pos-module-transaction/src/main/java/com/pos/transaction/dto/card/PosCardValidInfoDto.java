/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.card;

import java.io.Serializable;

/**
 * 信用卡验证信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
public class PosCardValidInfoDto implements Serializable {

    private static final long serialVersionUID = -5077933435804626554L;

    private String cvv2;

    private String validYear;//有效年

    private String validMonth;//有效月

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getValidYear() {
        return validYear;
    }

    public void setValidYear(String validYear) {
        this.validYear = validYear;
    }

    public String getValidMonth() {
        return validMonth;
    }

    public void setValidMonth(String validMonth) {
        this.validMonth = validMonth;
    }
}
