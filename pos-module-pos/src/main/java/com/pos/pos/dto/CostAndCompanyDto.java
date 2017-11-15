/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto;

import com.pos.pos.constants.CostTypeEnum;

import java.io.Serializable;

/**
 * @author 睿智
 * @version 1.0, 2017/8/30
 */
public class CostAndCompanyDto  implements Serializable{

    private static final long serialVersionUID = -3223546616848225373L;
    private long companyId;

    private CostTypeEnum costTypeEnum;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public CostTypeEnum getCostTypeEnum() {
        return costTypeEnum;
    }

    public void setCostTypeEnum(CostTypeEnum costTypeEnum) {
        this.costTypeEnum = costTypeEnum;
    }
}
