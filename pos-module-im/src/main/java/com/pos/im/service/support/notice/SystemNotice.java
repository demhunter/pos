/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice;

/**
 * 自定义系统通知抽象类
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public abstract class SystemNotice {

    /** @see SystemNoticeType#code */
    protected Byte noticeType; // 自定义系统通知类型

    public SystemNoticeType parseNoticeType() {
        return noticeType == null ? null : SystemNoticeType.getEnum(noticeType);
    }

    public Byte getNoticeType() {
        return noticeType;
    }

    public abstract String toString();

}
