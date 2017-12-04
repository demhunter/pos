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
     * POS 客户等级配置
     */
    public static final String POS_CUSTOMER_LEVEL_CONFIG = "pos_customer_level_config:";

    /**
     * POS 服务器当前配置所有客户等级
     */
    public static final String POS_CUSTOMER_LEVELS = "pos_customer_levels:";

    /**
     * POS 客户关系树结构
     */
    public static final String POS_CUSTOMER_RELATION_TREE = "pos_customer_relation_tree:";

    /**
     * POS 客户关系树节点信息
     */
    public static final String POS_CUSTOMER_RELATION_NODE = "pos_customer_relation_node:";

    /**
     * POS 客户的直接下级集合
     */
    public static final String POS_CUSTOMER_RELATION_NODE_CHILDREN = "pos_customer_relation_node_children:";

}
