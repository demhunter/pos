/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.user.constant.PlatformEmployeeType;

import java.io.Serializable;

/**
 * 平台家居顾问详细信息Dto
 *
 * @author wangbing
 * @version 1.0, 2017/7/6
 */
public class PlatformEmployeeDetailDto implements Serializable {

    private static final long serialVersionUID = -5089347937751902848L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("服务区域")
    private Long areaId;

    @ApiModelProperty("编号（工号）")
    private String workNo;

    /**
     * @see PlatformEmployeeType
     */
    @ApiModelProperty("用户细分类型（1：客服经理，2：普通家居顾问）")
    private byte userDetailType;

    @ApiModelProperty("用户细分类型描述")
    public String getUserDetailTypeDesc() {
        PlatformEmployeeType type = parseUserDetailType();

        return type == null ? "" : type.getDesc();
    }

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("是否接受分配")
    private boolean distribution;

    @ApiModelProperty("是否离职")
    private boolean quit;

    @ApiModelProperty("是否启用")
    private boolean enable;

    public PlatformEmployeeType parseUserDetailType() {
        return userDetailType == 0 ? null : PlatformEmployeeType.getEnum(userDetailType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(byte userDetailType) {
        this.userDetailType = userDetailType;
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

    public boolean isDistribution() {
        return distribution;
    }

    public void setDistribution(boolean distribution) {
        this.distribution = distribution;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
