package com.pos.basic.dto.multimedia;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lifei
 * @version 1.0, 2017/9/22
 */
public class CaseMaterialDto implements Serializable {

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

    @ApiModelProperty(value = "施工还是竣工")
    private String caseMaterialType;

    @ApiModelProperty(value = "时间")
    private Date bulidDate;

    public Byte getTitleId() {
        return titleId;
    }

    public void setTitleId(Byte titleId) {
        this.titleId = titleId;
    }

    public String getCaseMaterialType() {
        return caseMaterialType;
    }

    public void setCaseMaterialType(String caseMaterialType) {
        this.caseMaterialType = caseMaterialType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Date getBulidDate() {
        return bulidDate;
    }

    public void setBulidDate(Date bulidDate) {
        this.bulidDate = bulidDate;
    }
}
