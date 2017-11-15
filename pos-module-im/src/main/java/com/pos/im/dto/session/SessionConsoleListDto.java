/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/10/25
 */
public class SessionConsoleListDto implements Serializable {

    private static final long serialVersionUID = -2299652237728653487L;

    @ApiModelProperty("会话ID，自增id")
    private Long id;

    @ApiModelProperty("会话的头像")
    private String headImage;

    @ApiModelProperty("v1.9.0 * 更换IM服务商后对接IM平台的群组ID")
    private String groupId;

    @ApiModelProperty("会话名称，同时也是对接IM平台的群组名称")
    private String name;

    @ApiModelProperty("呼叫次数")
    private short callTotal;

    @ApiModelProperty("群组状态    0--正常  1--重要  2--风险")
    private int groupState;

    @ApiModelProperty("群组状态描述")
    private String groupStateDesc;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最新会话时间")
    private Date lastTalkTime;

    @ApiModelProperty("作品名称")
    private String caseName;

    @ApiModelProperty("公司名")
    private String companyName;

    @ApiModelProperty("会话状态     0---已关闭   1---未关闭")
    private int state;

    @ApiModelProperty("会话状态描述")
    private String stateDesc;

    @ApiModelProperty("加入状态   1---已加入   2---未加入")
    private int joinState;

    @ApiModelProperty("加入状态描述")
    private String joinStateDesc;

    @ApiModelProperty("IMUid")
    private String imUid;

    @ApiModelProperty("IMToken")
    private String imToken;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getCallTotal() {
        return callTotal;
    }

    public void setCallTotal(short callTotal) {
        this.callTotal = callTotal;
    }

    public int getGroupState() {
        return groupState;
    }

    public void setGroupState(int groupState) {
        this.groupState = groupState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTalkTime() {
        return lastTalkTime;
    }

    public void setLastTalkTime(Date lastTalkTime) {
        this.lastTalkTime = lastTalkTime;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGroupStateDesc() {
        return groupStateDesc;
    }

    public void setGroupStateDesc(String groupStateDesc) {
        this.groupStateDesc = groupStateDesc;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public int getJoinState() {
        return joinState;
    }

    public void setJoinState(int joinState) {
        this.joinState = joinState;
    }

    public String getJoinStateDesc() {
        return joinStateDesc;
    }

    public void setJoinStateDesc(String joinStateDesc) {
        this.joinStateDesc = joinStateDesc;
    }

    public String getImUid() {
        return imUid;
    }

    public void setImUid(String imUid) {
        this.imUid = imUid;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }
}
