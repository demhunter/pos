/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class RelationCustomerCondition {

    private String refuse;

    private String notRefuse;

    private Long relation;

    private String mobilePhone;

    private Long peId;

    public Long getPeId() {
        return peId;
    }

    public void setPeId(Long peId) {
        this.peId = peId;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }

    public String getNotRefuse() {
        return notRefuse;
    }

    public void setNotRefuse(String notRefuse) {
        this.notRefuse = notRefuse;
    }

    public Long getRelation() {
        return relation;
    }

    public void setRelation(Long relation) {
        this.relation = relation;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
