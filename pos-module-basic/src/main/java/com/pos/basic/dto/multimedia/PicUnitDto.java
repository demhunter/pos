/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.multimedia;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.PrintableBeanUtils;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;
import java.util.List;

/**
 * 图片展示单元Dto
 *
 * @author cc
 * @version 1.0, 16/8/26
 */
public class PicUnitDto implements Serializable {

    private static final long serialVersionUID = 780527641747519903L;
    /**
     * 标题id（用于枚举时）(int)
     */
    @ApiModelProperty(value = "标题id（用于枚举时）(int)")
    private Byte titleId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 图片（列表，如只有一张，则取第一张）
     */
    @ApiModelProperty(value = "图片（列表，如只有一张，则取第一张）")
    private List<String> pics;

    /**
     * 文字说明
     */
    @ApiModelProperty(value = "文字说明")
    private String explain;

    public static String toJson(List<PicUnitDto> list) {
        if (list != null && !list.isEmpty()) {
            return JsonUtils.objectToJson(list);
        } else {
            return null;
        }
    }

    public static List<PicUnitDto> fromJson(String jsonStr) {
        if (jsonStr != null && !jsonStr.isEmpty()) {
            return JsonUtils.jsonToObject(jsonStr, new TypeReference<List<PicUnitDto>>() {
            });
        } else {
            return null;
        }
    }

    public Byte getTitleId() {
        return titleId;
    }

    public void setTitleId(Byte titleId) {
        this.titleId = titleId;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
