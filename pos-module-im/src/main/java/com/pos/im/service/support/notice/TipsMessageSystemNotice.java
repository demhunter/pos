/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice;

import com.pos.common.util.basic.JsonUtils;
import com.pos.im.service.support.notice.tips.Tips;

/**
 * 小灰条自定义系统通知
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public class TipsMessageSystemNotice extends SystemNotice {

    private Tips content; // 小灰条消息内容


    public TipsMessageSystemNotice(Tips content) {
        this.noticeType = SystemNoticeType.TIPS_MESSAGE_NOTICE.getCode();
        this.content = content;
    }

    public Tips getContent() {
        return content;
    }


    @Override
    public String toString() {
        return JsonUtils.objectToJsonIgnoreNull(this);
    }
}
