package com.pos.user.domain;

import com.pos.user.constant.PlatformEmployeeType;

import java.io.Serializable;
import java.util.Date;

/**
 * 公司平台的业者 家居顾问以及客服经理
 * Created by 睿智 on 2017/7/3.
 */
public class PlatformEmployee implements Serializable {

    private static final long serialVersionUID = -2095275633710985475L;

    private Long id;

    private Long userId;

    private Long areaId;//服务区域

    private String workNo;//编号（工号）

    /**
     * @see PlatformEmployeeType
     */
    private byte userDetailType; // 用户细分类型

    private String nickName; // 昵称

    private String headImage; // 头像

    private boolean distribution;//是否接受分配

    private boolean quit;//是否离职

    private boolean enable;//是否启用

    private Date createDate;

    private Date updateDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
