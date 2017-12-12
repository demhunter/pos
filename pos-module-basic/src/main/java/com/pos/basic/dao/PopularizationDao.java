/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.condition.query.PopularizationCondition;
import com.pos.basic.domain.PopularizationDocument;
import com.pos.basic.dto.popularization.PopularizationDocumentDto;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推广相关Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
@Repository
public interface PopularizationDao {

    /**
     * 新增推广文案
     *
     * @param document 文案信息
     */
    void save(@Param("document") PopularizationDocument document);

    /**
     * 更新推广文案
     *
     * @param document 文案信息
     */
    void update(@Param("document") PopularizationDocument document);

    /**
     * 获取指定推广文案信息
     *
     * @param documentId 文案id
     * @return 文案信息
     */
    PopularizationDocument get(@Param("documentId") Long documentId);

    /**
     * 启用、禁用推广文案
     *
     * @param documentId   文案id
     * @param available    true：启用；false：禁用
     * @param updateUserId 更新操作人
     */
    void updateDocumentAvailable(Long documentId, boolean available, Long updateUserId);

    /**
     * 查询符合条件的推广文案数量
     *
     * @param condition 查询条件
     * @return 文案数量
     */
    int getDocumentCount(@Param("condition") PopularizationCondition condition);

    /**
     * 查询符合条件的文案列表
     *
     * @param condition   查询条件
     * @param orderHelper 排序参数
     * @param limitHelper 分页参数
     * @return 文案列表
     */
    List<PopularizationDocument> queryDocuments(
            @Param("condition") PopularizationCondition condition,
            @Param("orderHelper") OrderHelper orderHelper,
            @Param("limitHelper") LimitHelper limitHelper);
}
