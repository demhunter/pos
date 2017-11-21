/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/10/19
 */
public class DataLogDto implements Serializable {

    private static final long serialVersionUID = 4047519148131461682L;

    @ApiModelProperty("用户ID")
    private long userId;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("设备唯一标识")
    private String equipmentId;

    @ApiModelProperty("用户行为")
    private int action;

    @ApiModelProperty("用户行为来源")
    private int actionResource;

    @ApiModelProperty("用户行为结果")
    private int actionResult;

    @ApiModelProperty("设备型号")
    private String equipmentVersion;

    @ApiModelProperty("设备操作系统")
    private String equipmentSystem;

    @ApiModelProperty("设备操作系统版本")
    private String equipmentSystemVersion;

    @ApiModelProperty("用户行为结果提示语句")
    private String remark;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("IP归属地")
    private String ipAddress;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getActionResource() {
        return actionResource;
    }

    public void setActionResource(int actionResource) {
        this.actionResource = actionResource;
    }

    public int getActionResult() {
        return actionResult;
    }

    public void setActionResult(int actionResult) {
        this.actionResult = actionResult;
    }

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }

    public String getEquipmentSystem() {
        return equipmentSystem;
    }

    public void setEquipmentSystem(String equipmentSystem) {
        this.equipmentSystem = equipmentSystem;
    }

    public String getEquipmentSystemVersion() {
        return equipmentSystemVersion;
    }

    public void setEquipmentSystemVersion(String equipmentSystemVersion) {
        this.equipmentSystemVersion = equipmentSystemVersion;
    }
}
