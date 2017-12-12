/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic.AddressSupport;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/9/11
 */
public class SinaData implements Serializable {

    private static final long serialVersionUID = 3046855902224000931L;
    private Byte ret;//请求是否成功

    private String start;//ip起始区间

    private String end; //ip终止区间

    private String country;//国家

    private String province;//省份

    private String city;//城市

    private String district;//

    private String isp; //运营商

    private String type; //

    private String desc;//

    public Byte getRet() {
        return ret;
    }

    public void setRet(Byte ret) {
        this.ret = ret;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
