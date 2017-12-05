/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.dao;

import com.pos.authority.domain.CustomerRelation;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.common.util.mvc.support.LimitHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户关系Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/4
 */
@Repository
public interface CustomerRelationDao {

    /**
     * 保存客户关系信息
     *
     * @param relation 保存客户关系信息
     */
    void save(@Param("relation") CustomerRelation relation);

    /**
     * 获取关联关系信息
     *
     * @param limitHelper 分页参数
     * @return 关联关系信息列表
     */
    List<CustomerRelationDto> getRelations(@Param("limitHelper") LimitHelper limitHelper);

    /**
     * 根据用户id查询上下客户关系
     *
     * @param childUserId 用户id
     * @return 上下客户关系
     */
    CustomerRelationDto getByChildUserId(@Param("childUserId") Long childUserId);
}