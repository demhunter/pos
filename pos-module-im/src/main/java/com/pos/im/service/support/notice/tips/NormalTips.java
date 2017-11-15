/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.notice.tips;

import com.pos.common.util.basic.JsonUtils;

/**
 * 普通文本小灰条消息
 *
 * @author wangbing
 * @version 1.0, 2017/9/13
 */
public class NormalTips extends Tips {

    // 小灰条消息文本内容
    private String message;

    // 小灰条消息拓展信息
    private String extra;

    public NormalTips(String message,String groupId) {
        this(message, null,groupId);
    }

    public NormalTips(String message, String extra,String groupId) {
        this.tipsType = TipsType.DEFAULT.getCode();
        this.message = message;
        this.extra = extra;
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public String getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return JsonUtils.objectToJsonIgnoreNull(this);
    }
}
