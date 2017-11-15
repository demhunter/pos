package com.pos.user.domain;

import com.pos.user.constant.CustomerOrderStatus;

import java.io.Serializable;

/**
 * Created by 睿智 on 2017/7/3.
 */
public class PECustomers implements Serializable {

    private static final long serialVersionUID = -4537387603615232267L;

    private Long id;

    private Long peUserId;//家居顾问的userid

    private Long cUserId;//客户的userid

    private Long areaId;//服务区域Id

    private String remark;//备注

    /**
     * @see CustomerOrderStatus
     */
    private byte status;//订单的状态

    private byte relationType;//关联的渠道

    public byte getRelationType() {
        return relationType;
    }

    public void setRelationType(byte relationType) {
        this.relationType = relationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeUserId() {
        return peUserId;
    }

    public void setPeUserId(Long peUserId) {
        this.peUserId = peUserId;
    }

    public Long getcUserId() {
        return cUserId;
    }

    public void setcUserId(Long cUserId) {
        this.cUserId = cUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}

