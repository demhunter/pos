/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.dto;

/**
 * 合利宝响应返回对象公共接口
 *
 * @author wangbing
 * @version 1.0, 2017/12/21
 */
public abstract class HelibaoBasicResponse {

    /**
     * 获取交易类型
     *
     * @return 交易类型
     */
    public abstract String getRt1_bizType();

    /**
     * 获取返回码
     *
     * @return 返回状态码
     */
    public abstract String getRt2_retCode();

    /**
     * 获取返回信息
     *
     * @return 返回信息
     */
    public abstract String getRt3_retMsg();

    /**
     * 获取商户编号
     *
     * @return 商户编号
     */
    public abstract String getRt4_customerNumber();

    /**
     * 获取返回数据签名
     *
     * @return 数据签名
     */
    public abstract String getSign();
}
