/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.v1_0_0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 清洗POS交易数据
 * <p>
 * 1、清洗交易数据的付款银行卡信息，主要作用为解密SQL变更的付款银行卡信息数据
 * </p>
 *
 * @author wangbing
 * @version 1.0, 2017/10/25
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class RepairDataV1_0_0 {

    private final static Logger logger = LoggerFactory.getLogger(RepairDataV1_0_0.class);

}
