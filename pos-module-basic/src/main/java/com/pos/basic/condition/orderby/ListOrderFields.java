/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.condition.orderby;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 列表排序字段
 * <p>
 *     Todo cc: 太笼统，不易于维护，参考OrderField接口及其实现类
 * </p>
 * @author cc
 * @version 1.0, 16/9/7
 */
public class ListOrderFields {

    public static List<String> IM_ORDER_FIELDS;

    public static Map<String, String> FIELDS_ALIAS_MAP;

    static {
        IM_ORDER_FIELDS = Lists.newArrayList(
                "contactCount",// 电话联系次数
                "currentChatCount",// 当前聊天数
                "totalChatCount"// 总聊天数
        );

        FIELDS_ALIAS_MAP = Maps.newHashMap();
        FIELDS_ALIAS_MAP.put("updateTime", "updateDate");
        FIELDS_ALIAS_MAP.put("collectCount", "collectionCount");
    }
}
