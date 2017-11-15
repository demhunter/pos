/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice;

import com.pos.common.util.basic.JsonUtils;
import com.pos.im.dto.message.InternalMessageDto;

/**
 * 站内信自定义系统通知
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public class InternalMessageSystemNotice extends SystemNotice {

    private InternalMessageDto content; // 站内信消息内容

    public InternalMessageSystemNotice(InternalMessageDto content) {
        this.noticeType = SystemNoticeType.INTERNAL_MESSAGE_NOTICE.getCode();
        this.content = content;
    }

    public InternalMessageDto getContent() {
        return content;
    }

    @Override
    public String toString() {
        return JsonUtils.objectToJsonIgnoreNull(this);
    }
}
