/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.merchant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.pos.user.constant.UserType;
import com.pos.user.dto.UserDto;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.Copyable;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * B端用户DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class MerchantDto extends UserDto implements Serializable, Copyable<MerchantDto> {

    private static final long serialVersionUID = -7377582918748181739L;

    /**
     * 账号实体的UID（区分于用户ID），对象转换时需要该属性
     */
    @JsonIgnore
    private Long entityId;

    @ApiModelProperty("用户细分类型(int)：暂无，预留字段")
    private byte userDetailType;

    @ApiModelProperty("所属公司ID")
    private Long companyId;

    @ApiModelProperty("头像")
    private String headImage;

    @Override
    public MerchantDto copy() {
        MerchantDto newObj = new MerchantDto();
        BeanUtils.copyProperties(this, newObj);
        return newObj;
    }

    @Override
    public void check(String fieldPrefix) {
        super.check(fieldPrefix);
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(entityId, fieldPrefix + "entityId");
        FieldChecker.checkEmpty(companyId, fieldPrefix + "companyId");
    }

    @Override
    public String getShowName() {
        return getName();
    }

    @Override
    public String getUserType() {
        return Strings.isNullOrEmpty(super.getUserType()) ? UserType.BUSINESS.getValue() : super.getUserType();
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(byte userDetailType) {
        this.userDetailType = userDetailType;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

}