/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.condition.orderby.PopularizationOrderField;
import com.pos.basic.condition.query.PopularizationCondition;
import com.pos.basic.dao.PopularizationDao;
import com.pos.basic.domain.PopularizationDocument;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.popularization.PopularizationDocumentDto;
import com.pos.basic.exception.BasicErrorCode;
import com.pos.basic.service.PopularizationService;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.mvc.support.*;
import com.pos.common.util.validation.FieldChecker;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推广相关ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PopularizationServiceImpl implements PopularizationService {

    @Resource
    private PopularizationDao popularizationDao;

    @Override
    public ApiResult<NullObject> addOrUpdateDocument(PopularizationDocumentDto documentInfo, UserIdentifier operator) {
        FieldChecker.checkEmpty(documentInfo, "document");
        FieldChecker.checkEmpty(operator, "operator");
        documentInfo.check();

        if (documentInfo.getId() == null) {
            // 新增
            PopularizationDocument documentDomain = new PopularizationDocument();
            documentDomain.setDocument(documentInfo.getDocument());
            if (!CollectionUtils.isEmpty(documentInfo.getImages())) {
                documentDomain.setImages(JsonUtils.objectToJson(documentInfo.getImages()));
            }
            documentDomain.setCreateUserId(operator.getUserId());
            popularizationDao.save(documentDomain);
        } else {
            // 更新
            PopularizationDocument documentDomain = popularizationDao.get(documentInfo.getId());
            if (documentDomain == null) {
                return ApiResult.fail(BasicErrorCode.POPULARIZATION_DOCUMENT_NOT_EXISTED);
            }

            documentDomain.setDocument(documentInfo.getDocument());
            if (!CollectionUtils.isEmpty(documentInfo.getImages())) {
                documentDomain.setImages(JsonUtils.objectToJson(documentInfo.getImages()));
            }
            documentDomain.setUpdateUserId(operator.getUserId());
            popularizationDao.update(documentDomain);
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult<PopularizationDocumentDto> findDocument(Long documentId) {
        FieldChecker.checkEmpty(documentId, "documentId");

        PopularizationDocument documentDomain = popularizationDao.get(documentId);
        if (documentDomain == null) {
            return ApiResult.fail(BasicErrorCode.POPULARIZATION_DOCUMENT_NOT_EXISTED);
        }


        return ApiResult.succ(toPopularizationDocumentDto(documentDomain));
    }

    private PopularizationDocumentDto toPopularizationDocumentDto(PopularizationDocument documentDomain) {
        PopularizationDocumentDto dto = new PopularizationDocumentDto();

        BeanUtils.copyProperties(documentDomain, dto);
        if (!StringUtils.isEmpty(documentDomain.getImages())) {
            dto.setImages(JsonUtils.jsonToObject(documentDomain.getImages(), new TypeReference<List<String>>() {}));
        }

        return dto;
    }

    @Override
    public ApiResult<NullObject> updateDocumentAvailable(Long documentId, boolean available, UserIdentifier operator) {
        FieldChecker.checkEmpty(documentId, "documentId");
        FieldChecker.checkEmpty(operator, "operator");

        PopularizationDocument documentDomain = popularizationDao.get(documentId);
        if (documentDomain == null) {
            return ApiResult.fail(BasicErrorCode.POPULARIZATION_DOCUMENT_NOT_EXISTED);
        }
        popularizationDao.updateDocumentAvailable(documentId, available, operator.getUserId());

        return ApiResult.succ();
    }

    @Override
    public ApiResult<Pagination<PopularizationDocumentDto>> queryDocuments(PopularizationCondition condition, OrderHelper orderHelper, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(PopularizationOrderField.getInterface());
        }

        int totalCount = popularizationDao.getDocumentCount(condition);
        Pagination<PopularizationDocumentDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<PopularizationDocument> documents = popularizationDao.queryDocuments(condition, orderHelper, limitHelper);
            if(!CollectionUtils.isEmpty(documents)) {
                pagination.setResult(documents.stream().map(this::toPopularizationDocumentDto).collect(Collectors.toList()));
            }
        }

        return ApiResult.succ(pagination);
    }
}
