/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.export;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 省份导出Dto
 *
 * @author cc
 * @version 1.0, 2016/11/17
 */
public class ProvinceExportDto implements Serializable {

    private static final long serialVersionUID = 2190404013743723027L;
    private Long id;

    private String name;

    private List<CityExportDto> cities;

    public List<CityExportDto> getCities() {
        return cities;
    }

    public void setCities(List<CityExportDto> cities) {
        this.cities = cities;
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

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
