/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌DTO.
 *
 * @author wayne
 * @version 1.0, 2016/8/29
 */
@ApiModel
public class BrandDto implements Serializable {

    private static final long serialVersionUID = -5755457755831042699L;

    @ApiModelProperty("品牌编号")
    private Long id;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌LOGO图片URL")
    private String logo;

    @ApiModelProperty("品牌官网")
    private String website;

    @ApiModelProperty("是否可用")
    private boolean available;

    @ApiModelProperty("品牌分类列表")
    private List<BrandClassDto> brandClasses;

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(name, fieldPrefix + "name");
        FieldChecker.checkEmpty(logo, fieldPrefix + "logo");

        final String classesPrefix = fieldPrefix + "brandClasses";
        FieldChecker.checkEmpty(brandClasses, classesPrefix);
        brandClasses.forEach(brandClass -> brandClass.check(classesPrefix));
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<BrandClassDto> getBrandClasses() {
        return brandClasses;
    }

    public void setBrandClasses(List<BrandClassDto> brandClasses) {
        this.brandClasses = brandClasses;
    }

}