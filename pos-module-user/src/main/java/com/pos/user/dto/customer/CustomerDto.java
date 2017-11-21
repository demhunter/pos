/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.pos.user.constant.UserType;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.Copyable;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.dto.UserDto;
import com.pos.user.session.UserSession;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * C端用户DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class CustomerDto extends UserDto implements Serializable, Copyable<CustomerDto> {

    private static final long serialVersionUID = 5897963366243976312L;

    /**
     * 账号实体的UID（区分于用户ID），对象转换时需要该属性
     */
    @JsonIgnore
    private Long entityId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("用户会话信息")
    private UserSession userSession;

    @ApiModelProperty("用户分类")
    private Byte customerType;

    @ApiModelProperty("用户分类名")
    private String customerTypeDesc;

    @ApiModelProperty("是否为意向客户")
    private Boolean intention;

    @ApiModelProperty("备注信息")
    private String remarks;

    /**
     * 所属推客
     */
    @ApiModelProperty("客户推客的关系id")
    private Long customerTwitterId;

    @ApiModelProperty("所属推客姓名")
    private String twitterName;

    @ApiModelProperty("所属推客电话")
    private String twitterPhone;

    @Override
    public CustomerDto copy() {
        CustomerDto newObj = new CustomerDto();
        BeanUtils.copyProperties(this, newObj);
        return newObj;
    }

    @Override
    public void check(String fieldPrefix) {
        super.check(fieldPrefix);
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(entityId, fieldPrefix + "entityId");
        if (!Strings.isNullOrEmpty(nickName)) {
            Validator.checkNickName(nickName);
        }
    }

    @Override
    public String getShowName() {
        return !Strings.isNullOrEmpty(nickName) ? nickName : SimpleRegexUtils.hiddenMobile(getUserPhone());
    }

    @Override
    public String getUserType() {
        return Strings.isNullOrEmpty(super.getUserType()) ? UserType.CUSTOMER.getValue() : super.getUserType();
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Boolean getIntention() {
        return intention;
    }

    public void setIntention(Boolean intention) {
        this.intention = intention;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCustomerTwitterId() {
        return customerTwitterId;
    }

    public void setCustomerTwitterId(Long customerTwitterId) {
        this.customerTwitterId = customerTwitterId;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public String getTwitterPhone() {
        return twitterPhone;
    }

    public void setTwitterPhone(String twitterPhone) {
        this.twitterPhone = twitterPhone;
    }

    public String getCustomerTypeDesc() {
        return customerTypeDesc;
    }

    public void setCustomerTypeDesc(String customerTypeDesc) {
        this.customerTypeDesc = customerTypeDesc;
    }
}