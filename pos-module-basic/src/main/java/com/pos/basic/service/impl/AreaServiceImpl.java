/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.google.common.collect.Lists;
import com.pos.basic.constant.CharteredCitiesType;
import com.pos.basic.data.AreaCacheContainer;
import com.pos.basic.dto.area.AreaDto;
import com.pos.basic.dto.export.AreaExportDto;
import com.pos.basic.dto.export.CityExportDto;
import com.pos.basic.dao.AreaDao;
import com.pos.basic.domain.Area;
import com.pos.basic.dto.area.CitySupportDto;
import com.pos.basic.dto.export.ProvinceExportDto;
import com.pos.basic.service.AreaService;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.InitializationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 地区信息服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/6/15
 */
@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger LOG = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Resource
    private AreaCacheContainer areaCache;

    @Resource
    private AreaDao areaDao;

    @Override
    public ApiResult<List<AreaDto>> getProvinces() {
        try {
            List<AreaDto> list = areaCache.getProvinces();
            if (list != null) {
                return ApiResult.succ(list);
            } else {
                return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
            }
        } catch (InitializationException e) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_INITIALIZED);
        }
    }

    @Override
    public ApiResult<List<AreaDto>> getCitiesByProvinceId(Long provinceId, boolean parent) {
        try {
            return getAreaList(areaCache.getCitiesByProvinceId(provinceId), parent);
        } catch (InitializationException e) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_INITIALIZED);
        }
    }

    @Override
    public ApiResult<List<AreaDto>> getCountiesByCityId(Long cityId, boolean parent) {
        try {
            return getAreaList(areaCache.getCountiesByCityId(cityId), parent);
        } catch (InitializationException e) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_INITIALIZED);
        }
    }

    @Override
    public ApiResult<AreaDto> getLeaf(Long areaId) {
        try {
            AreaDto area = areaCache.getLeaf(areaId);
            if (area != null) {
                return ApiResult.succ(area);
            } else {
                return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
            }
        } catch (InitializationException e) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_INITIALIZED);
        }
    }

    @Override
    public ApiResult<AreaDto> getLeafAndDefaultRoot(Long areaId) {
        try {
            AreaDto area = areaCache.getLeaf(areaId);
            if (area != null) {
                if (area.getParent() != null && area.getParent().getParent() == null) {
                    AreaDto root = new AreaDto();
                    BeanUtils.copyProperties(area.getParent(), root, "parent", "idPath", "namePath", "shortNamePath");
                    root.setIdPath(root.getId().toString());
                    root.setNamePath(root.getName());
                    root.setShortNamePath(root.getShortName());
                    area.getParent().setParent(root);
                    area.getParent().setLevel((byte) 2);
                    area.getParent().setIdPath(root.getId() + "," + area.getParent().getIdPath());
                    area.getParent().setNamePath(root.getName() + "." + area.getParent().getNamePath());
                    area.getParent().setShortNamePath(root.getShortName() + "." + area.getParent().getShortNamePath());
                    area.setLevel((byte) 3);
                    area.setIdPath(root.getId() + "," + area.getIdPath());
                    area.setNamePath(root.getName() + "." + area.getNamePath());
                    area.setShortNamePath(root.getShortName() + "." + area.getShortNamePath());
                }
                return ApiResult.succ(area);
            } else {
                return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
            }
        } catch (InitializationException e) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_INITIALIZED);
        }
    }

    @Override
    public String getNameById(Long id) {
        Area area = areaDao.getById(id);
        return area == null ? null : area.getName();
    }

    @Override
    @SuppressWarnings("all")
    public String generateAreaJson() {
        List<ProvinceExportDto> provinces = Lists.newArrayList();

        List<AreaDto> proAreaDtos = getProvinces().getData();
        for (AreaDto proAreaDto : proAreaDtos) {
            if (!(proAreaDto.getName().equals("四川省") || proAreaDto.getName().equals("重庆市"))) {
                continue;
            }
            ProvinceExportDto province = new ProvinceExportDto();
            province.setId(proAreaDto.getId());

            if (proAreaDto.getName().equals("北京市")
                    || proAreaDto.getName().equals("天津市")
                    || proAreaDto.getName().equals("重庆市")
                    || proAreaDto.getName().equals("上海市")
                    || proAreaDto.getName().equals("香港特别行政区")
                    || proAreaDto.getName().equals("澳门特别行政区")) {
                province.setName(proAreaDto.getName().substring(0, 2));

                CityExportDto cityExportDto = new CityExportDto();
                cityExportDto.setId(province.getId());
                cityExportDto.setName(proAreaDto.getName());

                List<AreaExportDto> areaExportDtos = Lists.newArrayList();
                List<AreaDto> areaDtos = getCitiesByProvinceId(proAreaDto.getId(), false).getData();
                for (AreaDto areaDto : areaDtos) {
                    AreaExportDto area = new AreaExportDto();
                    area.setId(areaDto.getId());
                    area.setName(areaDto.getName());

                    areaExportDtos.add(area);
                }

                cityExportDto.setAreas(areaExportDtos);

                province.setCities(Lists.newArrayList(cityExportDto));

                provinces.add(province);
            } else {
                province.setName(proAreaDto.getName());

                List<CityExportDto> cityExportDtos = Lists.newArrayList();
                List<AreaDto> citiesAreaDtos = getCitiesByProvinceId(proAreaDto.getId(), false).getData();
                for (AreaDto cityAreaDto : citiesAreaDtos) {
                    CityExportDto city = new CityExportDto();
                    city.setId(cityAreaDto.getId());
                    city.setName(cityAreaDto.getName());

                    List<AreaExportDto> areaExportDtos = Lists.newArrayList();
                    List<AreaDto> areaDtos = getCountiesByCityId(cityAreaDto.getId(), false).getData();
                    if (areaDtos == null) {
                        cityExportDtos.add(city);
                        continue;
                    }
                    for (AreaDto areaDto : areaDtos) {
                        AreaExportDto area = new AreaExportDto();
                        area.setId(areaDto.getId());
                        area.setName(areaDto.getName());

                        areaExportDtos.add(area);
                    }

                    city.setAreas(areaExportDtos);

                    cityExportDtos.add(city);
                }

                province.setCities(cityExportDtos);

                provinces.add(province);
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("areas.json");
            fileOutputStream.write(JsonUtils.objectToJson(provinces).getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CitySupportDto> getCitySupport() {
        List<CitySupportDto> cities = Lists.newArrayList();

        // 当前系统支持成都(2294)和重庆(2252)
        cities.add(new CitySupportDto(2294L, "成都市", "成都"));
        cities.add(new CitySupportDto(2252L, "重庆市", "重庆"));

        return cities;
    }

    @Override
    public List<CitySupportDto> getCityList(List<Long> caseCityIds) {
        FieldChecker.checkEmpty(caseCityIds, "caseCityIds");

        List<CitySupportDto> citySupportDtos = areaDao.getCityList(caseCityIds);
        return citySupportDtos;
    }

    @Override
    public Boolean IsCharteredCities(Long areaId) {
        for (CharteredCitiesType citiesType : CharteredCitiesType.values()){
            if (citiesType.getId() == areaId){
                return true;
            }
        }
        return false;
    }

    private ApiResult<List<AreaDto>> getAreaList(List<AreaDto> list, boolean parent) {
        if (list == null) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
        }
        if (!parent) {
            for (AreaDto area : list) {
                area.setParent(null);
            }
        }
        return ApiResult.succ(list);
    }

    private Area toEntity(AreaDto dto) {
        Area entity = new Area();
        entity.setId(dto.getId());
        entity.setLevel(dto.getLevel());
        entity.setName(dto.getName());
        entity.setShortName(dto.getShortName());
        entity.setOrderNum(dto.getOrderNum());
        entity.setAvailable(dto.isAvailable());
        if (dto.getParent() != null) {
            entity.setParentId(dto.getParent().getId());
        }
        return entity;
    }

}