/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.domain;

/**
 * 支付相关区域
 *
 * @author cc
 * @version 1.0, 2016/12/1
 */
public class PayArea {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 城市编码
     */
    private String cityCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
