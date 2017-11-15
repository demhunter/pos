/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.dto.brand.BrandCollectionDto;
import com.pos.basic.dto.brand.BrandDetailDto;
import com.pos.basic.dto.brand.BrandDto;
import com.pos.common.util.mvc.support.*;
import com.pos.basic.condition.orderby.BrandOrderField;
import com.pos.basic.dto.brand.BrandIdsDto;

import java.util.List;

/**
 * 品牌管理的服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/8/29
 */
public interface BrandService {

    /**
     * 添加一个品牌.
     *
     * @param brandDto   品牌信息
     * @param operatorId 操作人UID
     */
    ApiResult<NullObject> addBrand(BrandDto brandDto, Long operatorId);

    /**
     * 更新一个品牌(品牌分类只能新加, 不能取消已有的分类).
     *
     * @param brandDto   品牌信息
     * @param operatorId 操作人UID
     */
    ApiResult<NullObject> updateBrand(BrandDto brandDto, Long operatorId);

    /**
     * 获取指定ID的品牌信息.
     *
     * @param id           品牌ID
     * @param queryClasses 是否查询品牌的分类信息, 如果为true, 查询后将自动填充到BrandDto.brandClasses
     */
    ApiResult<BrandDto> getBrand(Long id, boolean queryClasses);

    /**
     * 查询一组指定ID的品牌列表.
     *
     * @param ids 一组指定的品牌ID
     * @return 不返回品牌的分类列表
     */
    List<BrandDto> findByIds(Long[] ids);

    /**
     * 查询指定类型的品牌列表.
     *
     * @param typeId    一级类型
     * @param subTypeId 子类型(可空)
     * @param available true表示只查询可用的品牌, false表示只查询不可用的品牌, null表示查询所有品牌
     * @return 不返回品牌的分类列表
     */
    List<BrandDto> findByTypes(short typeId, Short subTypeId, Boolean available);

    /**
     * 按条件查询指定品牌列表.
     *
     * @param likeName    如果不为空, 按品牌名称进行模糊查询
     * @param available   true表示只查询可用的品牌, false表示只查询不可用的品牌, null表示查询所有品牌
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link BrandOrderField}
     * @return 不返回品牌的分类列表
     */
    Pagination<BrandDto> findBrands(String likeName, Boolean available, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 品牌id列表转换为品牌详情
     *
     * @param brandIdsDtos List<BrandIdsDto>
     * @param brandId      一级品牌类型
     * @return 转换结果
     */
    BrandDetailDto convertBrandIds2BrandDetails(List<BrandIdsDto> brandIdsDtos, final Byte brandId);

    /**
     * 查询指定类型的品牌集合.
     *
     * @param typeId   一级类型
     * @param queryKey 搜索关键字
     * @return 品牌集合DTO
     */
    BrandCollectionDto findBrandCollection(short typeId, String queryKey);

}
