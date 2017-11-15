/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import java.io.Serializable;
import java.util.List;

/**
 * @author lifei
 * @version 1.0, 2017/9/7
 */
public class PECustomerCondition implements Serializable {

    private List<Long> peUserIds;//家居顾问Id列表

    public List<Long> getPeUserIds() {
        return peUserIds;
    }

    public void setPeUserIds(List<Long> peUserIds) {
        this.peUserIds = peUserIds;
    }
}
