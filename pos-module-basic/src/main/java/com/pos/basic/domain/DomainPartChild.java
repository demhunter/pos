/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

import com.pos.basic.constant.DemoByteEnum;

/**
 * DomainPartChild
 *
 * @author cc
 * @version 1.0, 2017/3/24
 */
public class DomainPartChild extends DomainPartParent{

    private DemoByteEnum hiDemo;

    public DemoByteEnum getHiDemo() {
        return hiDemo;
    }

    public void setHiDemo(DemoByteEnum hiDemo) {
        this.hiDemo = hiDemo;
    }
}
