/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.dto.brand.BrandUnitDto;
import com.pos.basic.condition.orderby.BrandOrderField;
import com.pos.basic.domain.Brand;
import com.pos.basic.domain.BrandClass;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 品牌相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/29
 */
@Repository
public interface BrandDao {

    void save(@Param("brand") Brand brand);

    void update(@Param("brand") Brand brand);

    Brand get(@Param("brandId") Long brandId);

    Brand getByName(@Param("name") String name);

    List<Brand> findByIds(@Param("ids") Long[] ids);

    List<Brand> findByTypes(@Param("typeId") byte typeId,
                            @Param("subTypeId") Short subTypeId, @Param("available") Boolean available);

    List<BrandUnitDto> findBrandUnits(@Param("typeId") byte typeId, @Param("queryKey") String queryKey);

    /**
     * 按条件统计指定品牌总数.
     *
     * @param likeName  如果不为空, 按品牌名称进行模糊查询
     * @param available true表示只查询可用的品牌, false表示只查询不可用的品牌, null表示查询所有品牌
     * @return 品牌总数
     */
    int getBrandsTotal(@Param("likeName") String likeName, @Param("available") Boolean available);

    /**
     * 按条件查询指定品牌列表.
     *
     * @param likeName    如果不为空, 按品牌名称进行模糊查询
     * @param available   true表示只查询可用的品牌, false表示只查询不可用的品牌, null表示查询所有品牌
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link BrandOrderField}
     * @return 不返回品牌的分类列表
     */
    List<Brand> findBrands(@Param("likeName") String likeName, @Param("available") Boolean available,
                           @Param("limitHelper") LimitHelper limitHelper, @Param("orderHelper") OrderHelper orderHelper);

    /**
     * Brand Class - Begin -
     */

    void saveBrandClasses(@Param("list") List<BrandClass> classList);

    List<BrandClass> findBrandClasses(@Param("brandId") Long brandId);

    /** Brand Class - End - */

}
