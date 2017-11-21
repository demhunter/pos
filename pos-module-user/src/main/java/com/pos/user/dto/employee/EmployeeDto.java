/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.dto.ArticleUnitDto;
import com.pos.common.util.basic.Copyable;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.EmployeeType;
import com.pos.user.constant.UserType;
import com.pos.user.dto.UserDto;
import com.pos.user.session.UserSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * B端从业者DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public class EmployeeDto extends UserDto implements Serializable, Copyable<EmployeeDto> {

    /**
     * 平台业者的虚拟公司ID
     */
    private static final Long VIRTUAL_COMPANY_ID_OF_PLATFORM_EMPLOYEES = 0L;

    /**
     * 账号实体的UID（区分于用户ID），对象转换时需要该属性
     */
    @JsonIgnore
    private Long entityId;

    @ApiModelProperty("用户细分类型(int)：1 = 设计师，2 = 项目经理，3 = 商务代表，10 = 家居顾问")
    private byte userDetailType;

    @ApiModelProperty("所属公司ID")
    private Long companyId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("个人形象照，可以多张(JSON)")
    private String lifePhotos;

    @ApiModelProperty("个人简介")
    private String resume;

    @ApiModelProperty("是否离职")
    private boolean quitJobs;

    @ApiModelProperty("是否公开电话号码")
    private boolean publicPhone;

    @ApiModelProperty("业者主材内购邀请码")
    private String invitationCode;

    /**
     * {@link ArticleUnitDto}
     */
    @JsonIgnore
    private String articles;

    @ApiModelProperty("用户会话信息")
    private UserSession userSession;

    /**
     * @see EmployeeServiceContentDto
     * 设计收费标准
     */
    @ApiModelProperty("设计收费标准")
    private String serviceContent;

    @ApiModelProperty("用户细分类型的值描述")
    private String userDetailTypeDesc;

    public String getUserDetailTypeDesc() {
        if (!StringUtils.isEmpty(userDetailTypeDesc)) {
            return userDetailTypeDesc;
        }
        EmployeeType type = parseUserDetailType();
        return type != null ? type.getDesc() : "";
    }

    public void setUserDetailTypeDesc(String userDetailTypeDesc) {
        this.userDetailTypeDesc = userDetailTypeDesc;
    }

    @Override
    public EmployeeDto copy() {
        EmployeeDto newObj = new EmployeeDto();
        BeanUtils.copyProperties(this, newObj);
        return newObj;
    }

    @Override
    public void check(String fieldPrefix) {
        super.check(fieldPrefix);
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(entityId, fieldPrefix + "entityId");
        FieldChecker.checkEmpty(companyId, fieldPrefix + "companyId");
        if (!Strings.isNullOrEmpty(nickName)) {
            Validator.checkNickName(nickName);
        }
        if (parseUserDetailType() == null) {
            throw new ValidationException("'" + fieldPrefix + "userDetailType'无效值");
        }
    }

    @Override
    public String getShowName() {
        return getName();
    }

    @Override
    public String getUserType() {
        return Strings.isNullOrEmpty(super.getUserType()) ? UserType.EMPLOYEE.getValue() : super.getUserType();
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    @ApiModelProperty("软文列表")
    public List<ArticleUnitDto> getArticleList() {
        return ArticleUnitDto.fromJson(articles);
    }

    /**
     * 获取业者所属的公司ID，如果是平台业者，则返回虚拟的平台公司ID.
     *
     * @return
     */
    public Long getCompanyId() {
        return !EmployeeType.PLATFORM_BD.compare(userDetailType)
                ? companyId : VIRTUAL_COMPANY_ID_OF_PLATFORM_EMPLOYEES;
    }

    public EmployeeType parseUserDetailType() {
        return EmployeeType.getEnum(userDetailType);
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

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getLifePhotos() {
        return lifePhotos;
    }

    public void setLifePhotos(String lifePhotos) {
        this.lifePhotos = lifePhotos;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isQuitJobs() {
        return quitJobs;
    }

    public void setQuitJobs(boolean quitJobs) {
        this.quitJobs = quitJobs;
    }

    public boolean isPublicPhone() {
        return publicPhone;
    }

    public void setPublicPhone(boolean publicPhone) {
        this.publicPhone = publicPhone;
    }

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }
}