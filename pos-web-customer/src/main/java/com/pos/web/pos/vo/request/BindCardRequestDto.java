/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.vo.request;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
public class BindCardRequestDto implements Serializable {

    private static final long serialVersionUID = 6546700650829298285L;
    private String cardNO;

    private String name;

    private String idCardNO;

    private String phone;

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardNO() {
        return idCardNO;
    }

    public void setIdCardNO(String idCardNO) {
        this.idCardNO = idCardNO;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
