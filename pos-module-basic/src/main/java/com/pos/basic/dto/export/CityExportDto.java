/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.export;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 城市导出Dto
 *
 * @author cc
 * @version 1.0, 2016/11/17
 */
public class CityExportDto implements Serializable {

    private Long id;

    private String name;

    private List<AreaExportDto> areas;

    public List<AreaExportDto> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaExportDto> areas) {
        this.areas = areas;
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
