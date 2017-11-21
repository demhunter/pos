/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.statistics;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台数据统计-业者分类统计DTO
 *
 * @author wangbing
 * @version 1.0, 2017/03/31
 */
@Deprecated
public class EmployeeTypeStatisticsDto implements Serializable {

    public static final Byte NOT_LIMIT = 1; // 不限，与RELATED相对，不限是否关联作品

    public static final Byte RELATED = 2; // 已关联作品

    @ApiModelProperty("所有业者(key = 1(不限)，key = 2(已关联作品))")
    private Map<Byte, EmployeeTypeItemStatisticsDto> all;

    @ApiModelProperty("曾经登陆过的业者(key = 1(不限)，key = 2(已关联作品))")
    private Map<Byte, EmployeeTypeItemStatisticsDto> logined;

    @ApiModelProperty("从未登录过的业者(key = 1(不限)，key = 2(已关联作品))")
    private Map<Byte, EmployeeTypeItemStatisticsDto> notLogined;

    @ApiModelProperty("最近7天未登录过的业者(key = 1(不限)，key = 2(已关联作品))")
    private Map<Byte, EmployeeTypeItemStatisticsDto> latestNotLogined;

    public EmployeeTypeStatisticsDto() {
        all = initMap();
        logined = initMap();
        notLogined = initMap();
        latestNotLogined = initMap();

    }

    private Map<Byte, EmployeeTypeItemStatisticsDto> initMap() {
        Map<Byte, EmployeeTypeItemStatisticsDto> map = new HashMap<>();
        map.put(NOT_LIMIT, new EmployeeTypeItemStatisticsDto());
        map.put(RELATED, new EmployeeTypeItemStatisticsDto());
        return map;
    }

    public Map<Byte, EmployeeTypeItemStatisticsDto> getAll() {
        return all;
    }

    public void setAll(Map<Byte, EmployeeTypeItemStatisticsDto> all) {
        this.all = all;
    }

    public Map<Byte, EmployeeTypeItemStatisticsDto> getLogined() {
        return logined;
    }

    public void setLogined(Map<Byte, EmployeeTypeItemStatisticsDto> logined) {
        this.logined = logined;
    }

    public Map<Byte, EmployeeTypeItemStatisticsDto> getNotLogined() {
        return notLogined;
    }

    public void setNotLogined(Map<Byte, EmployeeTypeItemStatisticsDto> notLogined) {
        this.notLogined = notLogined;
    }

    public Map<Byte, EmployeeTypeItemStatisticsDto> getLatestNotLogined() {
        return latestNotLogined;
    }

    public void setLatestNotLogined(Map<Byte, EmployeeTypeItemStatisticsDto> latestNotLogined) {
        this.latestNotLogined = latestNotLogined;
    }
}
