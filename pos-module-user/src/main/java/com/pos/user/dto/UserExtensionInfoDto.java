/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户拓展信息
 * 
 * @author wangbing
 * @version 1.0, 2016/12/15
 */
public class UserExtensionInfoDto implements Serializable {

    private static final long serialVersionUID = -7575050227245414787L;

    @ApiModelProperty("设备唯一码")
    private String equipmentNo;

    @ApiModelProperty("机型")
    private String equipmentModel;

    @ApiModelProperty("操作系统版本信息")
    private String systemInfo;

    @ApiModelProperty("网络类型")
    private String netType;

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
