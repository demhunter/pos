/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.PrintableBeanUtils;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;
import java.util.List;

/**
 * 软文单元Dto
 *
 * @author cc
 * @version 1.0, 16/8/31
 */
public class ArticleUnitDto implements Serializable {

    private static final long serialVersionUID = -802917910076157308L;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 缩略图（url）
     */
    @ApiModelProperty(value = "缩略图（url）")
    private String thumb;

    /**
     * 文章url
     */
    @ApiModelProperty(value = "文章url")
    private String url;

    public static String toJson(List<ArticleUnitDto> list) {
        if (list != null && !list.isEmpty()) {
            return JsonUtils.objectToJson(list);
        } else {
            return null;
        }
    }

    public static List<ArticleUnitDto> fromJson(String jsonStr) {
        if (jsonStr != null && !jsonStr.isEmpty()) {
            return JsonUtils.jsonToObject(jsonStr, new TypeReference<List<ArticleUnitDto>>() {
            });
        } else {
            return null;
        }
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
