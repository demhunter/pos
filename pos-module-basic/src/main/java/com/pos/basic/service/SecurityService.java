/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

/**
 * SecurityService
 *
 * @author cc
 * @version 1.0, 2016/11/1
 */
public interface SecurityService {

    /**
     * 解密数据
     *
     * @param dataStr 加密的数据字符串
     * @return 解密后的数据字符串
     */
    String decryptData(String dataStr);

    /**
     * 加密数据
     *
     * @param dataStr 原数据字符串
     * @return 加密后的数据字符串
     */
    String encryptData(String dataStr);
}
