/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dto.develop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发展下级推客的概要信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/10/13
 */
public class DevelopGeneralInfoDto implements Serializable {

    private static final long serialVersionUID = 667992829504482313L;

    @ApiModelProperty("佣金费率（BigDecimal，返回具体数值，如万分之2，则返回0.0002）")
    private BigDecimal rate;

    @ApiModelProperty("累计发展下级推客人数")
    private Integer developCount;

    @ApiModelProperty("上级推客userId")
    private Long parentUserId;

    @ApiModelProperty("是否存在上级推客，true：存在，false：不存在")
    private boolean existedParent;

    @ApiModelProperty("上级推客姓名")
    private String parentName;

    // @ApiModelProperty("上级推客头像")
    @JsonIgnore
    private String parentHeadImage;

    @ApiModelProperty("上级推客电话")
    private String parentPhone;

    public boolean getExistedParent() {
        return existedParent;
    }

    public void setExistedParent(boolean existedParent) {
        this.existedParent = existedParent;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getDevelopCount() {
        return developCount;
    }

    public void setDevelopCount(Integer developCount) {
        this.developCount = developCount;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentHeadImage() {
        return parentHeadImage;
    }

    public void setParentHeadImage(String parentHeadImage) {
        this.parentHeadImage = parentHeadImage;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }
}
