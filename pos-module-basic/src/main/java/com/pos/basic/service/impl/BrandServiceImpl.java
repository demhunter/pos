/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pos.basic.condition.orderby.BrandOrderField;
import com.pos.basic.constant.BrandType;
import com.pos.basic.dao.BrandDao;
import com.pos.basic.domain.Brand;
import com.pos.basic.domain.BrandClass;
import com.pos.basic.dto.brand.*;
import com.pos.basic.exception.BasicErrorCode;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.mq.MQTemplate;
import com.pos.basic.service.BrandService;
import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.brand.*;
import com.pos.basic.mq.MQMessage;
import com.pos.common.util.codec.SimpleURLCodec;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 品牌管理的服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/8/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BrandServiceImpl implements BrandService {

    static final Logger LOG = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Resource
    private BrandDao brandDao;

    @Resource
    private MQTemplate mqTemplate;

    @Override
    public ApiResult<NullObject> addBrand(BrandDto brandDto, Long operatorId) {
        FieldChecker.checkEmpty(operatorId, "operatorId");
        FieldChecker.checkEmpty(brandDto, "brandDto");
        brandDto.check("brandDto");

        if (brandDao.getByName(brandDto.getName()) != null) {
            return ApiResult.fail(BasicErrorCode.BRAND_EXISTED);
        }

        Date nowTime = Calendar.getInstance().getTime();
        Brand brand = new Brand().setName(brandDto.getName())
                .setLogo(brandDto.getLogo()).setAvailable(true)
                .setCreateUserId(operatorId).setCreateTime(nowTime)
                .setWebsite(brandDto.getWebsite());
        brandDao.save(brand);

        List<BrandClass> brandClasses = brandDto.getBrandClasses().stream()
                .map(brandClassDto -> new BrandClass()
                        .setBrandId(brand.getId())
                        .setType(brandClassDto.getType().byteValue())
                        .setSubType(brandClassDto.getSubType().shortValue())
                        .setCreateUserId(operatorId).setCreateTime(nowTime))
                .collect(Collectors.toList());
        brandDao.saveBrandClasses(brandClasses);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> updateBrand(BrandDto brandDto, Long operatorId) {
        FieldChecker.checkEmpty(operatorId, "operatorId");
        FieldChecker.checkEmpty(brandDto, "brandDto");
        brandDto.check("brandDto");

        Brand brand = brandDto.getId() != null ? brandDao.get(brandDto.getId()) : null;
        if (brand == null || !brand.isAvailable()) {
            return ApiResult.fail(BasicErrorCode.BRAND_NOT_FOUND);
        }

        // 检查品牌名称是否已经存在
        if (!brand.getName().equals(brandDto.getName())
                && brandDao.getByName(brandDto.getName()) != null) {
            return ApiResult.fail(BasicErrorCode.BRAND_EXISTED);
        }

        Date nowTime = Calendar.getInstance().getTime();
        Brand newBrand = new Brand().setId(brandDto.getId())
                .setName(brandDto.getName()).setLogo(brandDto.getLogo())
                .setUpdateUserId(operatorId).setUpdateTime(nowTime)
                .setWebsite(brandDto.getWebsite());
        brandDao.update(newBrand);

        // 过滤已添加的分类后再保存
        List<BrandClass> brandClasses = brandDao.findBrandClasses(brandDto.getId());
        List<BrandClass> newBrandClasses = brandDto.getBrandClasses().stream()
                .filter(brandClassDto -> searchBrandClass(brandClasses, brandClassDto) == null)
                .map(brandClassDto -> new BrandClass()
                        .setBrandId(brandDto.getId())
                        .setType(brandClassDto.getType().byteValue())
                        .setSubType(brandClassDto.getSubType().shortValue())
                        .setCreateUserId(operatorId).setCreateTime(nowTime))
                .collect(Collectors.toList());
        if (!newBrandClasses.isEmpty()) {
            brandDao.saveBrandClasses(newBrandClasses);
        }

        // 发送更新品牌消息
        mqTemplate.sendDirectMessage(new MQMessage(MQReceiverType.CONSOLE, "brand.update.route.key", newBrand));

        return ApiResult.succ();
    }

    @Override
    public ApiResult<BrandDto> getBrand(Long id, boolean queryClasses) {
        FieldChecker.checkEmpty(id, "id");
        Brand brand = brandDao.get(id);
        if (brand == null || !brand.isAvailable()) {
            return ApiResult.fail(BasicErrorCode.BRAND_NOT_FOUND);
        }

        BrandDto brandDto = convert2Dto(brand);
        if (queryClasses) {
            List<BrandClass> brandClasses = brandDao.findBrandClasses(id);
            if (brandClasses != null && !brandClasses.isEmpty()) {
                brandDto.setBrandClasses(brandClasses.stream()
                        .map(brandClass -> convert2Dto(brandClass))
                        .collect(Collectors.toList()));
            }
        }

        return ApiResult.succ(brandDto);
    }

    @Override
    public List<BrandDto> findByIds(Long[] ids) {
        FieldChecker.checkEmpty(ids, "ids");
        List<Brand> list = brandDao.findByIds(ids);
        return list != null && !list.isEmpty() ? list.stream()
                .map(brand -> convert2Dto(brand)).collect(Collectors.toList()) : null;
    }

    @Override
    public List<BrandDto> findByTypes(short typeId, Short subTypeId, Boolean available) {
        List<Brand> list = brandDao.findByTypes((byte) typeId, subTypeId, available);
        return list != null && !list.isEmpty() ? list.stream()
                .map(brand -> convert2Dto(brand)).collect(Collectors.toList()) : null;
    }

    @Override
    public Pagination<BrandDto> findBrands(String likeName, Boolean available,
                                           LimitHelper limitHelper, OrderHelper orderHelper) {
        likeName = Strings.emptyToNull(likeName);
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(BrandOrderField.getInterface());
        }

        int totalCount = brandDao.getBrandsTotal(likeName, available);
        Pagination<BrandDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<Brand> list = brandDao.findBrands(likeName, available, limitHelper, orderHelper);
            pagination.setResult(list != null && !list.isEmpty() ? list.stream()
                    .map(brand -> convert2Dto(brand)).collect(Collectors.toList()) : null);
        }

        return pagination;
    }

    /**
     * 品牌id列表转换为品牌详情
     *
     * @param brandIdsDtos List<BrandIdsDto>
     * @param brandId      一级品牌类型
     * @return 转换结果
     */
    @SuppressWarnings("all")
    public BrandDetailDto convertBrandIds2BrandDetails(List<BrandIdsDto> brandIdsDtos, final Byte brandId) {
        BrandDetailDto brandDetailDto = new BrandDetailDto();
        brandDetailDto.setBrands(Lists.<SubBrandDetailDto>newArrayList());

        Iterable<BrandIdsDto> filteredBrandIdsDtos = Iterables.filter(brandIdsDtos, new Predicate<BrandIdsDto>() {
            @Override
            public boolean apply(BrandIdsDto brandIdsDto) {
                if (brandIdsDto.getType().equals(brandId)) {
                    return true;
                }
                return false;
            }
        });

        if (filteredBrandIdsDtos == null || !filteredBrandIdsDtos.iterator().hasNext()) {
            return null;
        }

        Map<Integer, List<BrandIdsDto>> secondClassMap = Maps.newHashMap();
        for (BrandIdsDto idsDto : filteredBrandIdsDtos) {
            if (!secondClassMap.containsKey(new Integer(idsDto.getSubType()))) {
                secondClassMap.put(new Integer(idsDto.getSubType()), Lists.<BrandIdsDto>newArrayList(idsDto));
            } else {
                secondClassMap.get(new Integer(idsDto.getSubType())).add(idsDto);
            }
        }

        for (Map.Entry<Integer, List<BrandIdsDto>> entry : secondClassMap.entrySet()) {
            List<Long> brandIds = Lists.transform(entry.getValue(), new Function<BrandIdsDto, Long>() {
                @Override
                public Long apply(BrandIdsDto brandIdsDto) {
                    return brandIdsDto.getId();
                }
            });
            List<BrandDto> brandDtos = findByIds(brandIds.toArray(new Long[brandIds.size()]));
            SubBrandDetailDto subBrandDetailDto = new SubBrandDetailDto();
            subBrandDetailDto.setBrands(brandDtos2SimpleBrandDtos(brandDtos));
            subBrandDetailDto.setName(BrandType.getSubTypeName(brandId, entry.getKey().shortValue()));

            brandDetailDto.getBrands().add(subBrandDetailDto);
        }

        brandDetailDto.setName(BrandType.getEnum(brandId).name());

        return brandDetailDto;
    }

    @Override
    public BrandCollectionDto findBrandCollection(short typeId, String queryKey) {
        BrandType brandType = BrandType.getEnum((byte) typeId);
        if (brandType == null) {
            return null;
        }
        if (queryKey != null) {
            queryKey = SimpleURLCodec.decodeUtf8(queryKey);
        }

        BrandCollectionDto collection = new BrandCollectionDto();
        // 读取并设置类型
        collection.setId(Integer.valueOf(brandType.getCode()));
        collection.setName(brandType.name());
        collection.setSubTypes(new LinkedHashMap<>());
        for (BrandType.SubType subType : brandType.getChildList()) {
            Integer subKey = new Integer(subType.getValue());
            collection.getSubTypes().put(
                    subKey, new BrandCollectionDto(subKey, subType.getName()));
        }

        // 查询指定大类的品牌, 并将其归类到子类中
        List<BrandUnitDto> allBrands = brandDao.findBrandUnits((byte) typeId, queryKey);
        if (allBrands != null && !allBrands.isEmpty()) {
            for (BrandUnitDto brand : allBrands) {
                BrandCollectionDto subCollection = collection.getSubTypes().get(brand.getSubType());
                if (subCollection != null) {
                    if (subCollection.getBrands() == null) {
                        subCollection.setBrands(new ArrayList<>());
                    }
                    subCollection.getBrands().add(brand);
                } else {
                    LOG.warn("品牌无法进行归类：" + brand);
                }
            }
        }

        return collection;
    }

    private List<BrandSimpleDto> brandDtos2SimpleBrandDtos(List<BrandDto> brandDtos) {
        List<BrandSimpleDto> brandVos = Lists.newArrayList();

        for (BrandDto brandDto : brandDtos) {
            BrandSimpleDto brandVo = new BrandSimpleDto();
            brandVo.setName(brandDto.getName());
            brandVo.setLogo(brandDto.getLogo());
            brandVos.add(brandVo);
        }

        return brandVos;
    }

    private BrandDto convert2Dto(Brand entity) {
        BrandDto dto = new BrandDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLogo(entity.getLogo());
        dto.setAvailable(entity.isAvailable());
        dto.setWebsite(entity.getWebsite());
        return dto;
    }

    private BrandClassDto convert2Dto(BrandClass entity) {
        BrandClassDto dto = new BrandClassDto();
        dto.setId(entity.getId());
        dto.setBrandId(entity.getBrandId());
        dto.setType(Integer.valueOf(entity.getType()));
        dto.setSubType(new Integer(entity.getSubType()));
        return dto;
    }

    /**
     * 从分类实体列表中搜索指定的分类DTO.
     *
     * @param sourceList 分类实体源列表
     * @param targetDto  要搜索的分类DTO
     * @return 与分类DTO等同的分类Entity
     */
    private BrandClass searchBrandClass(List<BrandClass> sourceList, BrandClassDto targetDto) {
        for (BrandClass brandClass : sourceList) {
            if (brandClass.getType() == targetDto.getType().byteValue()
                    && brandClass.getSubType() == targetDto.getSubType().shortValue()) {
                return brandClass;
            }
        }
        return null;
    }

}