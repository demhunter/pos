/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.brand;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 品牌集合DTO.
 *
 * @author wayne
 * @version 1.0, 2017/2/23
 */
public class BrandCollectionDto implements Serializable {

    @ApiModelProperty("品牌类型编号")
    private Integer id;

    @ApiModelProperty("品牌类型名称")
    private String name;

    @ApiModelProperty("该类型下的品牌列表")
    private List<BrandUnitDto> brands;

    @ApiModelProperty("品牌子类集合, key = 子类ID")
    private Map<Integer, BrandCollectionDto> subTypes;

    public BrandCollectionDto() {
    }

    public BrandCollectionDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BrandUnitDto> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandUnitDto> brands) {
        this.brands = brands;
    }

    public Map<Integer, BrandCollectionDto> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(Map<Integer, BrandCollectionDto> subTypes) {
        this.subTypes = subTypes;
    }

}