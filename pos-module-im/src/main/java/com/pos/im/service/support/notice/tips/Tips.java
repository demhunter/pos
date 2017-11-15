/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice.tips;

/**
 * 小灰条消息抽象类
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public abstract class Tips {

    /** @see TipsType#code */
    protected Byte tipsType; // 小灰条消息类型

    protected String groupId;//群组的ID

    public Byte getTipsType() {
        return tipsType;
    }

    public TipsType parseTipsType() {
        return tipsType == null ? null : TipsType.getEnum(tipsType);
    }

    public String getGroupId(){return groupId;}

    public abstract String toString();
}
