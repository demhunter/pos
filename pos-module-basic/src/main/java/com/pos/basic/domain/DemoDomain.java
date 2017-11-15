/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import com.pos.basic.constant.DemoByteEnum;

/**
 * DemoDomain
 *
 * @author cc
 * @version 1.0, 2017/3/24
 */
public class DemoDomain {

    private Long id;

    private DemoByteEnum hiDemo;

    public DemoByteEnum getHiDemo() {
        return hiDemo;
    }

    public void setHiDemo(DemoByteEnum hiDemo) {
        this.hiDemo = hiDemo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
