/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

/**
 * Redis所使用到的常量
 *
 * @author cc
 * @version 1.0, 16/7/15
 */
public class RedisConstants {

    /**
     * 分隔符
     */
    public static final String SEP = ":";

    /**
     * 案例和公司映射关系
     */
    @Deprecated
    public static final String CASE_COM_MAP_KEY = "case_com_r:";

    /**
     * 公司每天的访客数
     */
    public static final String COM_VI_COUNT_PD_KEY = "com_vi_co:";

    /**
     * 公司的总访客数
     */
    public static final String COM_VI_TOTAL_KEY = "com_vi_to:";

    /**
     * 公司每天的访客集合
     */
    public static final String COM_VI_SET_PD_KEY = "com_vi_set:";

    /**
     * 公司每天的收藏数
     */
    public static final String COM_CO_COUNT_PD_KEY = "com_co_co:";

    /**
     * 公司的收藏总数
     */
    public static final String COM_CO_TOTAL_KEY = "com_co_to:";

    /**
     * 公司每天的聊天数
     */
    public static final String COM_IM_SESSION_PD_KEY = "com_im_co:";

    /**
     * 公司的聊天总数
     */
    public static final String COM_IM_SESSION_TOTAL_KEY = "com_im_to:";

    /**
     * 预约支付金额
     */
    public static final String BOOKING_FEE_KEY = "booking_fee";

    /**
     * 订单编号
     */
    public static final String ORDER_SERIAL_KEY = "order_serial";

    /**
     * 全局唯一id
     */
    public static final String UNIQUE_ID_TWELVE = "uni_id";

    /**
     * 支付上下文
     */
    public static final String PAY_CONTEXT_KEY = "pay_context:";

    /**
     * 分账上下文
     */
    public static final String DIVIDE_CONTEXT_KEY = "divide_context:";

    /**
     * 充值上下文（用户）
     */
    public static final String RECHARGE_CONTEXT_KEY = "recharge_context:";

    /**
     * 提现上下文
     */
    public static final String WITHDRAW_CONTEXT_KEY = "withdraw_context:";

    /**
     * 平台充值上下文
     */
    public static final String PLAT_RECHARGE_CONTEXT_KEY = "plat_recharge_context:";

    /**
     * 申诉时间（天）限制
     */
    public static final String APPEAL_LIMIT_KEY = "appeal_limit:";

    /**
     * 热词被搜索总次数
     */
    public static final String HOT_KEY = "hot_key";

    /**
     * 热词相关信息
     */
    public static final String HOT_KEY_INFO = "hot_key_info:";

    /**
     * 热词年周记录
     */
    public static final String HOT_KEY_WEEK = "hot_key_week:";

    /**
     * 热词年月记录
     */
    public static final String HOT_KEY_MONTH = "hot_key_month:";

    /**
     * 热词被搜索一次增加的score
     */
    public static final int HOT_KEY_ITEM_SCORE = 100;

    /**
     * 作品统计——房型
     */
    public static final String CASE_STATISTICS_HOUSE_TYPE = "case_sta_house_type";

    /**
     * 作品统计——风格
     */
    public static final String CASE_STATISTICS_DECORATE_STYLE = "case_sta_decorate_style";

    /**
     * 作品统计——分类
     */
    @Deprecated
    public static final String CASE_STATISTICS_DECORATE_TYPE = "case_sta_decorate_type";

    /**
     * 作品统计——完工分类
     */
    public static final String CASE_STATISTICS_DONE_DECORATE_TYPE = "case_sta_done_deco_type";

    /**
     * 作品统计——设计分类
     */
    public static final String CASE_STATISTICS_DESIGN_DECORATE_TYPE = "case_sta_design_deco_type";

    /**
     * 作品统计——面积类型
     */
    public static final String CASE_STATISTICS_HOUSE_AREA_TYPE = "case_sta_house_area_type";

    /**
     * 作品统计——城市
     */
    public static final String CASE_STATISTICS_CITY = "case_sta_city";

    /**
     * 作品统计——上架作品
     */
    public static final String CASE_STATISTICS_UP_CASE_TOTAL = "case_sta_up_total";

    /**
     * 作品统计——上架完工作品
     */
    public static final String CASE_STATISTICS_UP_DONE_TOTAL = "case_sta_up_done_total";

    /**
     * 作品统计——上架设计作品
     */
    public static final String CASE_STATISTICS_UP_DESIGN_TOTAL = "case_sta_up_design_total";

    /**
     * 推客结佣、提成总额
     */
    public static final String TWITTER_SETTLE_BONUS_TOTAL = "twitter_settle_bonus_total:";

    /**
     * 获取到了抵扣券的客户（推客报备客户）数量
     */
    public static final String TWITTER_CUSTOMER_COUNT_DEDUCTION = "twitter_customer_count_deduction:";

    /**
     * 所有客户（推客报备客户）获取到的优惠券总金额
     */
    public static final String TWITTER_CUSTOMER_TOTAL_DEDUCTION = "twitter_customer_total_deduction:";

    /**
     * 作品历史访问信息
     */
    public static final String CASE_HISTORY_VISIT = "case_his_vi:";

    /**
     * 友盟auth_token
     */
    public static final String UMENG_AUTH_TOKEN = "umeng_auth_token:";

    /**
     * 友盟appkey
     */
    public static final String UMENG_APP_KEY = "umeng_app_key:";

    /**
     * 友盟C端IOS事件Event
     */
    public static final String UMENG_C_IOS_EVENT = "umeng_c_ios_event:";

    /**
     * 友盟C端Android事件Event
     */
    public static final String UMENG_C_ANDROID_EVENT = "umeng_c_android_event:";

    /**
     * 友盟C端IOS事件Label
     */
    public static final String UMENG_C_IOS_LABEL = "umeng_c_ios_label:";

    /**
     * 友盟C端Android事件Label
     */
    public static final String UMENG_C_ANDROID_LABEL = "umeng_c_android_label:";

    /**
     * 友盟C端IOS事件Group
     */
    public static final String UMENG_C_IOS_GROUP = "umeng_c_ios_group:";

    /**
     * 友盟C端Android事件Group
     */
    public static final String UMENG_C_ANDROID_GROUP = "umeng_c_android_group:";

    /**
     * 友盟E端IOS事件Group
     */
    public static final String UMENG_E_IOS_GROUP = "umeng_e_ios_group:";

    /**
     * 友盟E端Android事件Group
     */
    public static final String UMENG_E_ANDROID_GROUP = "umeng_e_android_group:";

    /**
     * 预订单信息
     */
    public static final String PREDICT_ORDER_INFO = "predict_order_info:";

    /**
     * 账单最近一次支付金额
     */
    public static final String BILL_LATEST_PAY_AMOUNT = "bill_latest_pay_amount:";

    /**
     * 微信的access_token
     */
    public static final String WECHAT_ACCESS_TOKEN = "wechat_access_token:";

    /**
     * 微信的jsapi_ticket
     */
    public static final String WECHAT_JSAPI_TICKET = "wechat_jsapi_ticket:";

    /**
     * POS交易信息，主要记录与交相关的银行卡信息
     */
    public static final String POS_TRANSACTION_OUT_CARD_INFO = "pos_transaction_out_card:";

    /**
     * POS交易已提现队列，用于轮询交易状态
     */
    public static final String POS_TRANSACTION_WITHDRAW_QUEUE = "pos_transaction_withdraw_queue";

    /**
     * POS 短信推广手机号码队列
     */
    public static final String POS_SMS_SPREAD_PHONES_QUEUE = "pos_sms_spread_phones_queue";

    /**
     * POS 短信推广发送次数（以天记，每天会发送50个电话号码）
     */
    public static final String POS_SMS_SPREAD_TIMES = "pos_sms_spread_times";

}
