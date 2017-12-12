/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.multimedia;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.PrintableBeanUtils;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;

/**
 * 视频单元Dto
 *
 * @author cc
 * @version 1.0, 16/8/31
 */
public class VideoUnitDto implements Serializable {

    private static final long serialVersionUID = 8628153980504653519L;
    /**
     * 视频时长
     */
    @ApiModelProperty(value = "视频时长")
    private Integer duration;

    /**
     * 视频名称
     */
    @ApiModelProperty(value = "视频名称")
    private String name;

    /**
     * 视频缩略图所在的秒数
     */
    @ApiModelProperty(value = "视频缩略图所在的秒数")
    private Integer second;

    /**
     * 视频url
     */
    @ApiModelProperty(value = "视频url")
    private String url;

    /**
     * 饰品描述
     */
    @ApiModelProperty("饰品描述")
    private String description;

    public String toJson() {
        return JsonUtils.objectToJson(this);
    }

    public static VideoUnitDto fromJson(String jsonStr) {
        if (jsonStr != null && !jsonStr.isEmpty()) {
            return JsonUtils.jsonToObject(jsonStr, new TypeReference<VideoUnitDto>() {
            });
        } else {
            return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
