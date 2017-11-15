/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.area;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;

/**
 * 区域多级id Dto
 *
 * @author cc
 * @version 1.0, 16/9/12
 */
public class AreaIdsDto {

    /**
     * 区域一级id（省或者直辖市）
     */
    @ApiModelProperty("区域一级id（省或者直辖市）")
    private Long buildingLevelOne;

    /**
     * 区域一级名称
     */
    @ApiModelProperty("区域一级名称")
    private String levelOneName;

    /**
     * 区域二级id（市或者直辖市区）
     */
    @ApiModelProperty("区域二级id（市或者直辖市区）")
    private Long buildingLevelTwo;

    /**
     * 区域二级名称
     */
    @ApiModelProperty("区域二级名称")
    private String levelTwoName;

    /**
     * 区域三级id（区）
     */
    @ApiModelProperty("区域三级id（区）")
    private Long buildingLevelThree;

    /**
     * 区域三级名称
     */
    @ApiModelProperty("区域三级名称")
    private String levelThreeName;

    public String getLevelOneName() {
        return levelOneName;
    }

    public void setLevelOneName(String levelOneName) {
        this.levelOneName = levelOneName;
    }

    public String getLevelThreeName() {
        return levelThreeName;
    }

    public void setLevelThreeName(String levelThreeName) {
        this.levelThreeName = levelThreeName;
    }

    public String getLevelTwoName() {
        return levelTwoName;
    }

    public void setLevelTwoName(String levelTwoName) {
        this.levelTwoName = levelTwoName;
    }

    public Long getBuildingLevelOne() {
        return buildingLevelOne;
    }

    public void setBuildingLevelOne(Long buildingLevelOne) {
        this.buildingLevelOne = buildingLevelOne;
    }

    public Long getBuildingLevelThree() {
        return buildingLevelThree;
    }

    public void setBuildingLevelThree(Long buildingLevelThree) {
        this.buildingLevelThree = buildingLevelThree;
    }

    public Long getBuildingLevelTwo() {
        return buildingLevelTwo;
    }

    public void setBuildingLevelTwo(Long buildingLevelTwo) {
        this.buildingLevelTwo = buildingLevelTwo;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
