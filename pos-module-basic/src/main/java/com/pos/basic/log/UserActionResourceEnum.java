/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.log;

import com.pos.basic.enumHandler.CommonIntEnum;

import java.util.Objects;

/**
 * 用户行为来源枚举类
 * @author 睿智
 * @version 1.0, 2017/10/18
 */
public enum UserActionResourceEnum implements CommonIntEnum {

    ACTION_RESOURCE_START(1, 1, "直接启动"),

    ACTION_RESOURCE_START_BACKGROUND(2, 1, "后台启动"),

    ACTION_RESOURCE_LOGIN(3, 2, "APP-我的-立即登录"),

    ACTION_RESOURCE_MY_DISCOUNT(4, 2, "APP-我的-我的优惠"),

    ACTION_RESOURCE_MY_ORDER(5, 2, "APP-我的-我的订单"),

    ACTION_RESOURCE_MY_COLLECTION(6, 2, "APP-我的-我的收藏"),

    ACTION_RESOURCE_HISTORY(7, 2, "APP-我的-历史浏览"),

    ACTION_RESOURCE_ACTIVITY_BILL(8, 2, "APP-我的-活动账单"),

    ACTION_RESOURCE_PASSWORD(9, 2, "APP-我的-设置-密码设置"),

    ACTION_RESOURCE_COLLECTION_CASE(10, 2, "APP-作品-收藏作品"),

    ACTION_RESOURCE_SEND_COMMENTS(11, 2, "APP-作品-发送评论"),

    ACTION_RESOURCE_CREATE_CHAT(12, 2, "APP-作品-创建聊天"),

    ACTION_RESOURCE_SEE_MATERIAL_SUBSIDY(13, 2, "APP-优惠-查看主材补贴"),

    ACTION_RESOURCE_ENROLL(14, 2, " APP-优惠-立即报名"),

    ACTION_RESOURCE_CREATE_ORDER(15, 2, "APP-保障-立即下单"),

    ACTION_RESOURCE_CHAT_LOGIN(16, 2, "APP-聊天-立即登录"),

    ACTION_RESOURCE_OTHER(17, 2, "APP-其它"),

    ACTION_RESOURCE_POS(18, 2, "H5-鹦鹉收款"),

    ACTION_RESOURCE_DEVELOP_TWITTER(19, 2, " H5-发展推客"),

    ACTION_RESOURCE_REPORT_CUSTOMER(20, 2, " H5-报备客户"),

    ACTION_RESOURCE_MATERIAL_SUBSIDY(21, 2, " H5-主材补贴"),

    ACTION_RESOURCE_CASE_LIST(22, 3, "作品列表"),

    ACTION_RESOURCE_CASE_COLLECTION(23, 3, "我的收藏"),

    ACTION_RESOURCE_BROWSE_HISTORY(24, 3, "历史浏览"),

    ACTION_RESOURCE_SIMILAR_CASE(25, 3, "类似作品"),

    ACTION_RESOURCE_EMPLOYEE_ALL_CASE(26, 3, "业者-全部作品"),

    ACTION_RESOURCE_COMPANY_ALL_CASE(27, 3, "公司-全部作品"),

    ACTION_RESOURCE_GLOBAL_DISCOUNT(28, 4, "全局导航-优惠"),

    ACTION_RESOURCE_INDEX_DISCOUNT_ICON(29, 4, "首页-享真惠图标"),

    ACTION_RESOURCE_INDEX_DISCOUNT_BUTTON(30, 4, "首页-享真惠按钮"),

    ACTION_RESOURCE_GLOBAL_GUARANTEE(31, 5, "全局导航-保障"),

    ACTION_RESOURCE_CASE_TEAM(32, 5, "作品-原创团队"),

    ACTION_RESOURCE_INDEX_GUARANTEE_ICON(33, 5, "首页-定保障图标"),

    ACTION_RESOURCE_INDEX_GUARANTEE_BUTTON(34, 5, "首页-定保障按钮");

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

    UserActionResourceEnum(int code, int type, String desc) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public static UserActionResourceEnum getEnum(int code) {
        for (UserActionResourceEnum type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalStateException("非法的用户行为来源枚举类型code值！");
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
