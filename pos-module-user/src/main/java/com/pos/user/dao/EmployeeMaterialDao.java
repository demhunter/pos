/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.condition.query.RecommendCondition;
import com.pos.user.domain.EmployeeMaterial;
import com.pos.user.dto.employee.RecommendDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户业者主材邀请关系相关Dao
 *
 * @author lifei
 * @version 1.0, 2017/7/5
 */
@Repository
public interface EmployeeMaterialDao {

    /**
     * 查询业者客户主材内购关系信息
     *
     * @param customerId 客户ID
     * @return 关系信息
     */
    EmployeeMaterial findRelationByCustomerId(@Param("customerId") Long customerId);

    /**
     * 保存用户关系
     *
     * @param employeeMaterial
     */
    void saveEmployeeMaterial(@Param("employeeMaterial") EmployeeMaterial employeeMaterial);

    List<RecommendDto> getRecommendList(@Param("condition") RecommendCondition recommendCondition,
                                        @Param("limitHelper") LimitHelper limitHelper,
                                        @Param("orderHelper") OrderHelper orderHelper);

    int getRecommendListCount(@Param("condition") RecommendCondition recommendCondition);
}
