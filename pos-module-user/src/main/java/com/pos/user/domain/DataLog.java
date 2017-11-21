/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/10/19
 */
public class DataLog implements Serializable {

    private static final long serialVersionUID = 1047757170215514293L;

    private long id;

    private String equipmentId;

    private long userId;

    private Date createDate;

    private String actionResource;//行为来源

    private String actionResult;//行为结果

    private String remark;//备注

    private String ip;

    private String ipAddress;

    private String equipmentVersion;//设备型号

    private String equipmentSystem;//操作系统

    private String equipmentSystemVersion;//操作系统版本

    private String action;//用户的行为

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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

    public String getActionResource() {
        return actionResource;
    }

    public void setActionResource(String actionResource) {
        this.actionResource = actionResource;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
