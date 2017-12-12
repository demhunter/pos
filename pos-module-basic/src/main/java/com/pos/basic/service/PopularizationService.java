/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.condition.query.PopularizationCondition;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.popularization.PopularizationDocumentDto;
import com.pos.common.util.mvc.support.*;

/**
 * 推广相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public interface PopularizationService {

    /**
     * 新增或更新推广文案
     *
     * @param documentInfo 文案信息
     * @param operator     操作人标识
     * @return 操作结果
     */
    ApiResult<NullObject> addOrUpdateDocument(PopularizationDocumentDto documentInfo, UserIdentifier operator);

    /**
     * 查询指定的推广文案信息
     *
     * @param documentId 文案id
     * @return 文案信息
     */
    ApiResult<PopularizationDocumentDto> findDocument(Long documentId);

    /**
     * 启用、禁用推广文案
     *
     * @param documentId 文案id
     * @param available  true：启用；false：禁用
     * @param operator   操作人标识
     * @return 操作结果
     */
    ApiResult<NullObject> updateDocumentAvailable(Long documentId, boolean available, UserIdentifier operator);

    /**
     * 按条件查询推广文案列表
     *
     * @param condition   查询条件
     * @param orderHelper 排序参数
     * @param limitHelper 分页参数
     * @return 推广文案列表
     */
    ApiResult<Pagination<PopularizationDocumentDto>> queryDocuments(
            PopularizationCondition condition, OrderHelper orderHelper, LimitHelper limitHelper);

}
