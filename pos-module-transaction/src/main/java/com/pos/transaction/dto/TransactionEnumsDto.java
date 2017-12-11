/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto;

import com.pos.transaction.constants.PayModeEnum;
import com.pos.transaction.constants.TransactionStatusType;
import com.pos.transaction.constants.UserAuditStatus;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.CommonEnumDto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 快捷收款相关枚举Dto
 *
 * @author wangbing
 * @version 1.0, 2017/8/25
 */
public class TransactionEnumsDto implements Serializable {

    @ApiModelProperty("快捷收款打款方式枚举")
    private List<CommonEnumDto> payToolTypes;

    @ApiModelProperty("交易状态枚举")
    private List<CommonEnumDto> transactionStatusTypes;

    private static final TransactionEnumsDto instance = new TransactionEnumsDto();

    public static TransactionEnumsDto getInstance() {
        return instance;
    }

    private TransactionEnumsDto() {
        payToolTypes = Stream.of(PayModeEnum.values())
                .map(e -> new CommonEnumDto(e.getCode(), e.getDesc()))
                .collect(Collectors.toList());
        transactionStatusTypes = Stream.of(TransactionStatusType.values())
                .map(e -> new CommonEnumDto((byte) e.getCode(), e.getDesc()))
                .collect(Collectors.toList());
    }

    public List<CommonEnumDto> getPayToolTypes() {
        return payToolTypes;
    }

    public List<CommonEnumDto> getTransactionStatusTypes() {
        return transactionStatusTypes;
    }
}
