/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonIntEnum;
import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;

/**
 * 操作类型分类枚举定义
 *
 * @author wangbing
 * @version 1.0, 2017/12/9
 */
public enum OperationType implements CommonIntEnum, Serializable {

    实名认证(1, new SubOperationType[]{
            Certification.绑定结算银行卡, Certification.更换结算银行卡, Certification.其它
    }),

    快捷收款(2, new SubOperationType[]{
            EPOS.填卡下单, EPOS.选卡下单, EPOS.获取支付验证码, EPOS.确认支付, EPOS.提现到用户,
            EPOS.失败交易重发, EPOS.失败交易手动处理, EPOS.其它
    }),

    佣金提现(3, new SubOperationType[]{
            BrokerageWithdraw.发起佣金提现, BrokerageWithdraw.失败佣金提现重发,
            BrokerageWithdraw.失败佣金提现重发, BrokerageWithdraw.其它
    }),

    等级晋升(4, new SubOperationType[]{
            LevelUpgrade.提交等级晋升请求, LevelUpgrade.确认付款晋升, LevelUpgrade.其它
    });

    private final int code;

    private final SubOperationType[] children;

    OperationType(final int code, final SubOperationType[] children) {
        if (ArrayUtils.isEmpty(children)) {
            throw new NullPointerException("child operation must not empty!");
        }
        this.code = code;
        this.children = children;
    }

    @Override
    public int getCode() {
        return code;
    }

    public SubOperationType[] getChildren() {
        return children;
    }

    public boolean compare(int code) {
        return this.code == code;
    }

    public static OperationType getEnum(int code) {
        for (OperationType type : OperationType.values()) {
            if (type.compare(code)) {
                return type;
            }
        }

        return null;
    }

    public static OperationType getEnum(String symbol) {
        for (OperationType type : OperationType.values()) {
            if (type.name().equals(symbol)) {
                return type;
            }
        }

        return null;
    }

    /**
     * 获取父操作名称
     *
     * @param code 父操作类型code
     * @return 父操作名称
     */
    public static String getOperationName(int code) {
        OperationType type = OperationType.getEnum(code);
        return type == null ? null : type.name();
    }

    /**
     * 获取子操作
     *
     * @param parentCode 父操作code
     * @param childCode  子操作code
     * @return 子操作
     */
    public static OperationType.SubOperationType getSubOperation(int parentCode, int childCode) {
        OperationType parent = OperationType.getEnum(parentCode);
        if (parent != null) {
            for (OperationType.SubOperationType subType : parent.getChildren()) {
                if (subType.compare(childCode)) {
                    return subType;
                }
            }
        }
        return null;
    }

    /**
     * 获取子操作名称
     *
     * @param parentCode 父操作code
     * @param childCode  子操作code
     * @return 子操作名称
     */
    public static String getSubOperationName(int parentCode, int childCode) {
        OperationType parent = OperationType.getEnum(parentCode);
        if (parent != null) {
            for (OperationType.SubOperationType child : parent.getChildren()) {
                if (child.compare(childCode)) {
                    return child.getName();
                }
            }
        }
        return null;
    }

    /**
     * 操作子类型枚举接口
     */
    public interface SubOperationType {

        String getName();

        OperationType getParent();

        boolean compare(int code);

        int getCode();
    }

    /**
     * 实名认证子操作类型枚举定义
     */
    public enum Certification implements SubOperationType, CommonIntEnum {

        绑定结算银行卡(101, "绑定结算银行卡"),

        更换结算银行卡(102, "更换结算银行卡"),

        其它(199, "其它");

        private final int code;

        private final String desc;

        Certification(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getName() {
            return desc;
        }

        @Override
        public OperationType getParent() {
            return OperationType.实名认证;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 快捷收款子操作类型枚举定义
     */
    public enum EPOS implements SubOperationType, CommonIntEnum {

        填卡下单(201, "填卡下单"),

        选卡下单(202, "选卡下单"),

        获取支付验证码(203, "获取支付验证码"),

        确认支付(204, "确认支付"),

        提现到用户(205, "提现到用户"),

        失败交易重发(206, "失败交易重发"),

        失败交易手动处理(207, "失败交易手动处理"),

        其它(299, "其它");

        private final int code;

        private final String desc;

        EPOS(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getName() {
            return desc;
        }

        @Override
        public OperationType getParent() {
            return OperationType.快捷收款;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 佣金提现子操作类型枚举定义
     */
    public enum BrokerageWithdraw implements SubOperationType, CommonIntEnum {

        发起佣金提现(301, "发起佣金提现"),

        失败佣金提现重发(302, "失败佣金提现重发"),

        失败佣金提现手动处理(303, "失败佣金提现手动处理"),

        其它(399, "其它");

        private final int code;

        private final String desc;

        BrokerageWithdraw(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getName() {
            return desc;
        }

        @Override
        public OperationType getParent() {
            return OperationType.佣金提现;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 等级晋升子操作类型枚举定义
     */
    public enum LevelUpgrade implements SubOperationType, CommonIntEnum {

        提交等级晋升请求(401, "提交等级晋升请求"),

        确认付款晋升(402, "确认付款晋升"),

        其它(499, "其它");

        private final int code;

        private final String desc;

        LevelUpgrade(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getName() {
            return desc;
        }

        @Override
        public OperationType getParent() {
            return OperationType.等级晋升;
        }

        @Override
        public boolean compare(int code) {
            return this.code == code;
        }

        @Override
        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
