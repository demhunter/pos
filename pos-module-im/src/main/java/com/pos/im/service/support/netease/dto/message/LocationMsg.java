/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.message;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class LocationMsg {

    private String title;//中国 浙江省 杭州市 网商路 599号",    //地理位置title
    private double lng;//120.1908686708565,        // 经度
    private double lat;//30.18704515647036            // 纬度

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
