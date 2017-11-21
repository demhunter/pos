/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.log;

import com.pos.basic.enumHandler.CommonIntEnum;

import java.util.Objects;

/**
 * 用户行为枚举类
 * @author 睿智
 * @version 1.0, 2017/10/18
 */
public enum UserActionEnum implements CommonIntEnum {

    ACTION_START_APP(1, 1, "启动APP"),

    ACTION_LOGIN(2, 2, "登录"),

    ACTION_REGISTER(3, 2, "注册"),

    ACTION_FIND_PASSWORD(4, 2, "找回密码"),

    ACTION_SEE_CASE_LIST(5, 3, "查看作品列表"),

    ACTION_OPEN_CASE(6, 3, "打开作品"),

    ACTION_SEE_CASE_LIVE(7, 3, "查看作品现场实景"),

    ACTION_SEE_CASE_TEAM(8, 3, "查看作品原创团队"),

    ACTION_SEE_CASE_PROJECT(9, 3, "查看作品项目资料"),

    ACTION_CREATE_CHAT(10, 3, "创建聊天"),

    ACTION_SEE_DISCOUNT_LIST(11, 4, "查看优惠列表"),

    ACTION_SEE_DISCOUNT_DETAIL(12, 4, "查看优惠详情"),

    ACTION_SEE_MATERIAL_SUBSIDY(13, 4, "查看主材补贴"),

    ACTION_SEE_GUARANTEE_INDEX(14, 5, "查看保障首页"),

    ACTION_CLICK_PEOPLE_ASK(15, 5, "点击人工咨询"),

    ACTION_CLICK_APPLY_GUARANTEE(16, 5, "点击申请保障"),

    ACTION_SEE_CHAT_LIST(17, 6, "查看聊天列表"),

    ACTION_SEE_MY_INFO(18, 7, "查看我的页面");

    /**
     * 标识码
     */
    private final int code;

    /**
     * 类别
     */
    private final int type;


    /**
     * 描述内容
     */
    private final String desc;

    UserActionEnum(int code, int type, String desc) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public static UserActionEnum getEnum(int code) {
        for (UserActionEnum type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的用户行为枚举类型code值！");
    }

    @Override
    public int getCode() {
        return code;
    }


    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
