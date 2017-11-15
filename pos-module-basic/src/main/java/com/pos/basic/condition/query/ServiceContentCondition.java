/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.query;

import java.io.Serializable;

/**
 * 阶段服务内容的查询条件
 *
 * @author wangbing
 * @version 1.0, 2016/12/14
 */
public class ServiceContentCondition implements Serializable {

    /**
     * 自增ID
     */
    private Long id;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务类型，CompanyServiceContentType#code
     */
    private Byte type;

    public ServiceContentCondition(Long id) {
        this.id = id;
    }

    public ServiceContentCondition(String name) {
        this.name = name;
    }

    public ServiceContentCondition(Byte type) {
        this.type = type;
    }

    public ServiceContentCondition(Long id, String name, Byte type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
