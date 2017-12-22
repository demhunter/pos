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
     * 获取返回数据签名
     *
     * @return 数据签名
     */
    public abstract String getSign();
}
