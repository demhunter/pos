/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.data;

import com.pos.basic.dao.AreaDao;
import com.pos.basic.domain.Area;
import com.pos.basic.dto.area.AreaDto;
import com.pos.basic.dto.area.AreaIdsDto;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.InitializationException;
import com.pos.common.util.file.FileBytesStreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.springframework.beans.BeanUtils;

/**
 * 地区缓存容器.
 *
 * @author wayne
 * @version 1.0, 2016/6/16
 */
@Component
public class AreaCacheContainer {

    private static final Logger LOG = LoggerFactory.getLogger(AreaCacheContainer.class);

    @Resource
    private AreaDao areaDao;

    @Value("${area.cache.enable}")
    private boolean isEnable;

    private volatile boolean initialized;

    /**
     * 省/直辖市列表
     */
    private final List<AreaDto> provinces = new ArrayList<>(35);

    /**
     * 省/直辖市下属的城市列表, key = 省/直辖市ID, value = 该省/直辖市的城市列表
     */
    private final Map<Long, List<AreaDto>> provinceCities = new HashMap<>(50);

    /**
     * 城市下属的区县列表, key = 城市ID, value = 该城市的区县列表
     */
    private final Map<Long, List<AreaDto>> cityCounties = new HashMap<>(450);

    /**
     * 叶子节点集合, key = areaID, value则包含3级区县，以及2级直辖市的区县
     */
    private final Map<Long, AreaDto> leafMap = new HashMap<>(3200);

    /**
     * 初始化地区数据并加载到缓存容器中.
     *
     * @throws InitializationException 已经执行过初始化动作
     */
    @PostConstruct
    public void initialize() {
        if (!isEnable) {
            LOG.warn("AreaCache已被禁用，将无法正常调用AreaService！");
            return;
        }
        if (initialized) {
            throw new InitializationException(
                    this.getClass().getSimpleName() + "已经初始化！");
        }

        long startTime = System.currentTimeMillis();
        LOG.info("开始初始化地区数据...");
        Map<Long, AreaDto> tempMap = new HashMap<>(3500);
        try {
            // 初始化所有地区数据集合, 加载顺序不可以任意调换！
            initProvinces(tempMap);
            initProvinceCities(tempMap);
            initCityCounties(tempMap);
            initLeafMap();
            initialized = true;
            LOG.info("初始化地区数据完成！耗时：" + (System.currentTimeMillis() - startTime) + "ms");
            LOG.info("provinces[" + provinces.size() + "], provinceCities[" + provinceCities.size()
                    + "], cityCounties[" + cityCounties.size() + "], leafMap[" + leafMap.size() + "]");
        } catch (InitializationException e) {
            clear();
            LOG.error("初始化地区数据失败！cause: " + e.getMessage());
        } catch (Exception e) {
            clear();
            LOG.error("初始化地区数据失败！", e);
        } finally {
            tempMap.clear();
        }
    }

    /**
     * 获取所有一级地区列表(省/直辖市).
     *
     * @return 克隆对象列表
     * @throws InitializationException 尚未完成初始化
     */
    public List<AreaDto> getProvinces() {
        checkInitialized();
        return cloneDtoList(provinces);
    }

    /**
     * 获取指定省/直辖市下属的所有二级地区列表(市/区).
     *
     * @param provinceId 一级地区ID(省/直辖市)
     * @return 克隆对象列表
     * @throws InitializationException 尚未完成初始化
     */
    public List<AreaDto> getCitiesByProvinceId(Long provinceId) {
        checkInitialized();
        return provinceId != null ? cloneDtoList(provinceCities.get(provinceId)) : null;
    }

    /**
     * 获取指定市/区下属的所有三级地区列表(县/区等等).
     *
     * @param cityId 二级地区ID(市/区)
     * @return 克隆对象列表
     * @throws InitializationException 尚未完成初始化
     */
    public List<AreaDto> getCountiesByCityId(Long cityId) {
        checkInitialized();
        return cityId != null ? cloneDtoList(cityCounties.get(cityId)) : null;
    }

    /**
     * 获取指定叶子节点的地区信息.
     *
     * @param areaId 地区ID
     * @return 克隆对象，如果没有找到或者该地区不是叶子节点都返回null
     * @throws InitializationException 尚未完成初始化
     */
    public AreaDto getLeaf(Long areaId) {
        checkInitialized();
        if (areaId != null) {
            AreaDto area = leafMap.get(areaId);
            return area != null ? copyAreaDto(area) : null;
        }
        return null;
    }

    /**
     * 根据叶子节点获取全部节点id信息
     *
     * @param areaId 叶子节点id
     * @return 三级或两级节点id信息
     */
    public AreaIdsDto getAreaIds(Long areaId) {
        AreaIdsDto areaIdsDto = new AreaIdsDto();

        AreaDto areaDto = getLeaf(areaId);
        Byte level = areaDto.getLevel();
        if (level.equals((byte) 2)) {
            areaIdsDto.setBuildingLevelTwo(areaDto.getId());
            areaIdsDto.setLevelTwoName(areaDto.getName());
            areaIdsDto.setBuildingLevelOne(areaDto.getParent().getId());
            areaIdsDto.setLevelOneName(areaDto.getParent().getName());
        } else if (level.equals((byte) 3)) {
            areaIdsDto.setBuildingLevelThree(areaDto.getId());
            areaIdsDto.setLevelThreeName(areaDto.getName());
            areaIdsDto.setBuildingLevelTwo(areaDto.getParent().getId());
            areaIdsDto.setLevelTwoName(areaDto.getParent().getName());
            areaIdsDto.setBuildingLevelOne(areaDto.getParent().getParent().getId());
            areaIdsDto.setLevelOneName(areaDto.getParent().getParent().getName());
        }

        return areaIdsDto;
    }

    private void clear() {
        provinces.clear();
        provinceCities.clear();
        cityCounties.clear();
        leafMap.clear();
    }

    private void checkInitialized() throws InitializationException {
        if (!initialized) {
            throw new InitializationException(
                    this.getClass().getSimpleName() + "尚未初始化！");
        }
    }

    private AreaDto newInstanceDto(Area area) {
        AreaDto dto = new AreaDto();
        dto.setId(area.getId());
        dto.setLevel(area.getLevel());
        dto.setName(area.getName());
        dto.setShortName(area.getShortName());
        dto.setOrderNum(area.getOrderNum());
        dto.setAvailable(area.isAvailable());
        return dto;
    }

    private List<AreaDto> cloneDtoList(List<AreaDto> list) {
        if (list != null) {
            List<AreaDto> cloneList = list.stream()
                    .map(area -> copyAreaDto(area)).collect(Collectors.toList());
            return cloneList;
        } else {
            return null;
        }
    }

    private AreaDto copyAreaDto(AreaDto area) {
        AreaDto clone = new AreaDto();
        BeanUtils.copyProperties(area, clone, "parent");
        if (area.getParent() != null) {
            AreaDto cloneParent = new AreaDto();
            BeanUtils.copyProperties(area.getParent(), cloneParent, "parent");
            clone.setParent(cloneParent);
            if (area.getParent().getParent() != null) {
                AreaDto rootParent = new AreaDto();
                BeanUtils.copyProperties(area.getParent().getParent(), rootParent, "parent");
                clone.getParent().setParent(rootParent);
            }
        }
        return clone;
    }

    private void initProvinces(final Map<Long, AreaDto> tempMap) throws InitializationException {
        List<Area> areaList = areaDao.findAreaListByLevel((byte) 1);
        if (areaList == null || areaList.isEmpty()) {
            throw new InitializationException("一级地区数据为空");
        }

        for (Area area : areaList) {
            AreaDto dto = newInstanceDto(area);
            dto.setIdPath(area.getId().toString());
            dto.setNamePath(area.getName());
            dto.setShortNamePath(area.getShortName());
            provinces.add(dto);
            tempMap.put(dto.getId(), dto);
        }
    }

    private void initProvinceCities(final Map<Long, AreaDto> tempMap) throws InitializationException {
        List<Area> areaList = areaDao.findAreaListByLevel((byte) 2);
        if (areaList == null || areaList.isEmpty()) {
            throw new InitializationException("二级地区数据为空");
        }

        for (Area area : areaList) {
            AreaDto dto = newInstanceDto(area);
            AreaDto parent = getParentFromTempMap(area, tempMap);
            dto.setParent(parent);
            dto.setIdPath(parent.getId().toString()
                    + AreaDto.ID_PATH_SEPARATOR + area.getId().toString());
            dto.setNamePath(parent.getName()
                    + AreaDto.NAME_PATH_SEPARATOR + area.getName());
            dto.setShortNamePath(parent.getShortName()
                    + AreaDto.NAME_PATH_SEPARATOR + area.getShortName());

            List<AreaDto> childList = provinceCities.get(parent.getId());
            if (childList == null) {
                childList = new ArrayList<>();
            }
            childList.add(dto);
            provinceCities.put(parent.getId(), childList);
            tempMap.put(dto.getId(), dto);
        }
    }

    private void initCityCounties(final Map<Long, AreaDto> tempMap) throws InitializationException {
        List<Area> areaList = areaDao.findAreaListByLevel((byte) 3);
        if (areaList == null || areaList.isEmpty()) {
            throw new InitializationException("三级地区数据为空");
        }

        for (Area area : areaList) {
            AreaDto dto = newInstanceDto(area);
            AreaDto parent = getParentFromTempMap(area, tempMap);
            if (parent.getParent() == null) {
                throw new InitializationException("没有找到祖先节点. id = "
                        + area.getId() + ", parentId = " + area.getParentId());
            }
            dto.setParent(parent);
            dto.setIdPath(parent.getParent().getId().toString()
                    + AreaDto.ID_PATH_SEPARATOR + parent.getId().toString()
                    + AreaDto.ID_PATH_SEPARATOR + area.getId().toString());
            dto.setNamePath(parent.getParent().getName()
                    + AreaDto.NAME_PATH_SEPARATOR + parent.getName()
                    + AreaDto.NAME_PATH_SEPARATOR + area.getName());
            dto.setShortNamePath(parent.getParent().getShortName()
                    + AreaDto.NAME_PATH_SEPARATOR + parent.getShortName()
                    + AreaDto.NAME_PATH_SEPARATOR + area.getShortName());

            List<AreaDto> childList = cityCounties.get(parent.getId());
            if (childList == null) {
                childList = new ArrayList<>();
            }
            childList.add(dto);
            cityCounties.put(parent.getId(), childList);
            tempMap.put(dto.getId(), dto);
        }
    }

    private void initLeafMap() throws InitializationException {
        for (List<AreaDto> list : cityCounties.values()) {
            for (AreaDto area : list) {
                area.setLeaf(true);
                leafMap.put(area.getId(), area);
            }
        }

        for (List<AreaDto> list : provinceCities.values()) {
            for (AreaDto area : list) {
                if (area.getParent().getName().startsWith("北京")
                        || area.getParent().getName().startsWith("上海")
                        || area.getParent().getName().startsWith("天津")
                        || area.getParent().getName().startsWith("重庆")) {
                    area.setLeaf(true);
                    leafMap.put(area.getId(), area);
                }
            }
        }
    }

    private AreaDto getParentFromTempMap(Area area,
                                         final Map<Long, AreaDto> tempMap) throws InitializationException {
        if (area.getParentId() == null) {
            throw new InitializationException(
                    "非根级地区必须设置parentId. id = " + area.getId());
        }
        AreaDto parent = tempMap.get(area.getParentId());
        if (parent == null) {
            throw new InitializationException("没有找到parent. id = "
                    + area.getId() + ", parentId = " + area.getParentId());
        }
        if (area.getLevel() - 1 != parent.getLevel()) {
            throw new InitializationException("parent设置错误. id = "
                    + area.getId() + ", parentId = " + area.getParentId());
        }

        return parent;
    }

    /* 测试将地区数据按层级以JSON格式写入指定文件 -Begin- */

    private abstract class AreaJsonObject {

        private Long id;

        private String name;

        public AreaJsonObject(Long id, String name) {
            this.id = id;
            this.name = name;
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

    }

    private class ProvinceJsonObject extends AreaJsonObject {

        private List<CityJsonObject> cities;

        public ProvinceJsonObject(Long id, String name) {
            super(id, name);
        }

        public List<CityJsonObject> getCities() {
            return cities;
        }

        public void setCities(List<CityJsonObject> cities) {
            this.cities = cities;
        }

    }

    private class CityJsonObject extends AreaJsonObject {

        private List<CountyJsonObject> areas;

        public CityJsonObject(Long id, String name) {
            super(id, name);
        }

        public List<CountyJsonObject> getAreas() {
            return areas;
        }

        public void setAreas(List<CountyJsonObject> areas) {
            this.areas = areas;
        }

    }

    private class CountyJsonObject extends AreaJsonObject {

        public CountyJsonObject(Long id, String name) {
            super(id, name);
        }

    }

    private void writeToJsonFile(String filename) throws IOException {
        List<ProvinceJsonObject> provinces = new ArrayList<>();
        this.provinces.forEach(province -> {
            if (province.getName().startsWith("北京") || province.getName().startsWith("上海")
                    || province.getName().startsWith("天津") || province.getName().startsWith("重庆")) {
                ProvinceJsonObject provinceJson = new ProvinceJsonObject(province.getId(), province.getShortName());
                provinces.add(provinceJson);
                // get and set cities
                provinceJson.setCities(new ArrayList<>(1));
                CityJsonObject cityJson = new CityJsonObject(province.getId(), province.getName());
                provinceJson.getCities().add(cityJson);
                // get and set counties
                List<AreaDto> counties = provinceCities.get(province.getId());
                cityJson.setAreas(new ArrayList<>(counties.size()));
                counties.forEach(county -> {
                    CountyJsonObject countyJson = new CountyJsonObject(county.getId(), county.getName());
                    cityJson.getAreas().add(countyJson);
                });
            } else {
                ProvinceJsonObject provinceJson = new ProvinceJsonObject(province.getId(), province.getName());
                provinces.add(provinceJson);
                // get and set cities
                List<AreaDto> cities = provinceCities.get(province.getId());
                provinceJson.setCities(new ArrayList<>(cities.size()));
                cities.forEach(city -> {
                    CityJsonObject cityJson = new CityJsonObject(city.getId(), city.getName());
                    provinceJson.getCities().add(cityJson);
                    // get and set counties
                    List<AreaDto> counties = cityCounties.get(city.getId());
                    if (counties != null) {
                        cityJson.setAreas(new ArrayList<>(counties.size()));
                        counties.forEach(county -> {
                            CountyJsonObject countyJson = new CountyJsonObject(county.getId(), county.getName());
                            cityJson.getAreas().add(countyJson);
                        });
                    } else {
                        LOG.warn("没有3级区县的非直辖市：" + city);
                    }
                });
            }
        });

        FileBytesStreamUtils.write(JsonUtils.objectToJson(provinces).getBytes(), filename);
    }

    /**
     * TEST initialize.
     */
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("basic/applicationContext.xml");
        AreaCacheContainer bean = context.getBean(AreaCacheContainer.class); // spring将在初始化完成后自动调用initialize()
        bean.writeToJsonFile("C:\\area.json");
    }

    /* 测试将地区数据按层级以JSON格式写入指定文件 -End- */

}