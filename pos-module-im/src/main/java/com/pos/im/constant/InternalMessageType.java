/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.basic.enumHandler.CommonIntEnum;

/**
 * 站内消息的类型定义.
 *
 * @author wayne
 * @version 1.0, 2016/11/4
 */
public enum InternalMessageType implements CommonByteEnum {

    /**
     * 普通消息
     */
    DEFAULT((byte) 1, false, (byte) 1),
    /**
     * 会话消息
     */
    SESSION((byte) 2, false, (byte) 2),
    /**
     * 预约订单消息
     */
    @Deprecated
    BOOKING((byte) 3, false, (byte) 3),
    /**
     * 推客消息
     */
    TWITTER((byte) 4, true, (byte) 3),

    /**
     * 订单消息
     */
    ORDER((byte) 5, true, (byte) 3),

    /**
     * 主材内购消息
     */
    MATERIAL((byte) 6, false, (byte) 1);

    private final byte code;

    private final boolean subTypeRequired;

    // 消息查看类型：1 = 仅查看，2 = 跳转会话，3 = 跳转详情
    // ps：因旧版本原因，前端有做消息的本地化，为避免冲突，往后在新增查看类型时从6开始
    private final byte viewType;

    InternalMessageType(final byte code, final boolean subTypeRequired, byte viewType) {
        this.code = code;
        this.subTypeRequired = subTypeRequired;
        this.viewType = viewType;
    }

    public byte getCode() {
        return code;
    }

    public boolean isSubTypeRequired() {
        return subTypeRequired;
    }

    public boolean compare(byte code) {
        return this.code == code;
    }

    public byte getViewType() {
        return viewType;
    }

    public static InternalMessageType getEnum(byte code) {
        for (InternalMessageType type : InternalMessageType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 站内消息的子类型定义.
     */
    public enum SubType implements CommonIntEnum {

        /**
         * 推客提交申请
         */
        TWITTER_APPLY(401, "/template/twitter/twitter-index.shtml?close=1"),
        /**
         * 推客申请未通过
         */
        TWITTER_APPLY_REJECTED(402, "/template/twitter/twitter-index.shtml?close=1"),
        /**
         * 推客申请已通过
         */
        TWITTER_APPLY_AUTHORIZED(403, "/template/twitter/twitter-index.shtml?close=1"),
        /**
         * 推客邀请注册成功
         */
        TWITTER_INVITE_SUCCESSFUL(411, "/appv/#/developTwitter?closeType=1"),
        /**
         * 推客-客户已洽谈
         */
        TWITTER_CUSTOMER_CHATTING(421, "/template/twitter/customer-list-info.shtml?id=%d&close=1"),
        /**
         * 推客-客户已签约
         */
        TWITTER_CUSTOMER_SIGNED(422, "/template/twitter/twitter-pay-info.shtml?types=1&close=1"),
        /**
         * 推客-客户已结佣
         */
        TWITTER_CUSTOMER_SETTLED(423, "/template/twitter/twitter-pay-info.shtml?types=2&close=1"),
        /**
         * 推客-客户结佣奖励
         */
        TWITTER_CUSTOMER_SETTLED_REWARD(424, "/template/twitter/twitter-reward.shtml?types=1&close=1"),
        /**
         * 订单-创建订单
         */
        ORDER_CREATE(425, "/appv/#/orderDetails?orderId=%s&isCustomer=%s&index=%s&closeType=0"),
        /**
         * 订单-创建账单
         */
        ORDER_BILL_CREATE(426, "/appv/#/orderDetails?orderId=%s&isCustomer=%s&index=%s&closeType=0"),
        /**
         * 订单-支付账单
         */
        ORDER_BILL_PAYMENT(427, "/appv/#/orderDetails?orderId=%s&isCustomer=%s&index=%s&closeType=0"),
        /**
         * 订单-平台打款
         */
        ORDER_SETTLED(428, "/appv/#/orderDetails?orderId=%s&isCustomer=false&closeType=0"),
        /**
         * 订单-终止订单
         */
        ORDER_TERMINATED(429, "/appv/#/orderDetails?orderId=%s&isCustomer=%s&index=%s&closeType=0"),
        /**
         * 监理账单创建
         */
        SUPERVISION_ORDER_BILL_CREATE(430, "/appv/#/supervisionOrderDetailsForCustomer?orderId=%s&closeType=0&index=0"),
        /**
         * 监理订单关闭
         */
        SUPERVISION_ORDER_TERMINATED(431, "/appv/#/supervisionOrderDetailsForEmployee?orderId=%s&closeType=0&index=0"),
        /**
         * 监理定金账单支付
         */
        SUPERVISION_ORDER_EARNEST_BILL_PAYMENT(432, "/appv/#/supervisionOrderDetailsForEmployee?orderId=%s&closeType=0&index=0"),
        /**
         * 监理账单支付
         */
        SUPERVISION_ORDER_BILL_PAYMENT(432, "/appv/#/supervisionOrderDetailsForEmployee?orderId=%s&closeType=0&index=0");

        private final int code;

        private final String uri;

        SubType(final int code, final String uri) {
            this.code = code;
            this.uri = uri;
        }

        public int getCode() {
            return code;
        }

        public String getURI() {
            return uri;
        }

        public boolean compare(int code) {
            return this.code == code;
        }

        public static SubType getEnum(int code) {
            for (SubType type : SubType.values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return null;
        }

    }

}