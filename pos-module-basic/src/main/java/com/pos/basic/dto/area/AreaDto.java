/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.area;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 地区信息DTO.
 *
 * @author wayne
 * @version 1.0, 2016/6/15
 */
public class AreaDto implements Serializable {

    private static final long serialVersionUID = 1818519193185027138L;

    public static final String ID_PATH_SEPARATOR = ",";

    public static final String NAME_PATH_SEPARATOR = ".";

    @ApiModelProperty("地区ID")
    private Long id;

    @ApiModelProperty("地区名称")
    private String name;

    @ApiModelProperty("地区短名称")
    private String shortName;

    @ApiModelProperty("地区父节点")
    private AreaDto parent;

    @ApiModelProperty("地区层级(int)，目前设计最多为3级(直辖市只有2级)，1级为省，2级为市，3级为县/区")
    private byte level;

    @ApiModelProperty("显示排序（正序）(int)")
    private byte orderNum;

    @ApiModelProperty("是否可用")
    private boolean available;

    /* 以下为扩展字段 */

    @ApiModelProperty("是否为叶子节点")
    private boolean leaf;

    @ApiModelProperty("从根Area到当前Area的ID连接字符串, 以逗号分隔, 如: 1,2,3")
    private String idPath;

    @ApiModelProperty("从根Area到当前Area的名称连接字符串, 以点号分隔, 如: 四川省.成都市.高新区")
    private String namePath;

    @ApiModelProperty("从根Area到当前Area的短名称连接字符串, 以点号分隔, 如: 四川.成都.高新区")
    private String shortNamePath;

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public String acquireCityShortName() {
        if (!leaf) {
            return StringUtils.EMPTY;
        }

        String cityName = parent.getName();
        if (cityName.contains("市")) {
            return cityName.substring(0, cityName.length() - 1);
        } else {
            return cityName;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public AreaDto getParent() {
        return parent;
    }

    public void setParent(AreaDto parent) {
        this.parent = parent;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(byte orderNum) {
        this.orderNum = orderNum;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getIdPath() {
        return idPath;
    }

    public void setIdPath(String idPath) {
        this.idPath = idPath;
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath;
    }

    public String getShortNamePath() {
        return shortNamePath;
    }

    public void setShortNamePath(String shortNamePath) {
        this.shortNamePath = shortNamePath;
    }
}