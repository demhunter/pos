/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.common;

import com.pos.basic.condition.orderby.PopularizationOrderField;
import com.pos.basic.condition.query.PopularizationCondition;
import com.pos.basic.dto.popularization.PopularizationDocumentDto;
import com.pos.basic.service.PopularizationService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.session.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文案推广相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/12/1
 */
@RestController
@RequestMapping("/popularization/document")
@Api(value = "/popularization/document", description = "v2.0.0 * 文案推广相关接口")
public class PopularizationController {

    @Resource
    private PopularizationService popularizationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取推广文案列表", notes = "获取推广文案列表")
    public ApiResult<Pagination<PopularizationDocumentDto>> getDocuments(
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "文本搜索关键字")
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        PopularizationCondition condition = new PopularizationCondition();
        condition.setBeginTime(beginTime);
        condition.setEndTime(endTime);
        condition.setSearchKey(searchKey);

        LimitHelper limitHelper = LimitHelper.create(pageNum, pageSize);

        return popularizationService.queryDocuments(
                condition, PopularizationOrderField.getDefaultOrderHelper(), limitHelper);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 新增或修改推广文案", notes = "新增或修改推广文案")
    public ApiResult<NullObject> saveOrUpdateDocument(
            @ApiParam(name = "document", value = "推广文案信息")
            @RequestBody PopularizationDocumentDto document,
            @FromSession UserInfo userInfo) {
        return popularizationService.addOrUpdateDocument(document, userInfo.buildUserIdentifier());
    }

    @RequestMapping(value = "{documentId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取指定的推广文案信息", notes = "获取指定的推广文案信息")
    public ApiResult<PopularizationDocumentDto> getDocument(
            @ApiParam(name = "documentId", value = "推广文案id")
            @PathVariable("documentId") Long documentId) {
        return popularizationService.findDocument(documentId);
    }

    @RequestMapping(value = "{documentId}/available", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 启用/禁用推广文案", notes = "启用/禁用推广文案")
    public ApiResult<NullObject> updateAvailable(
            @ApiParam(name = "documentId", value = "推广文案id")
            @PathVariable("documentId") Long documentId,
            @ApiParam(name = "available", value = "true：启用；false：禁用")
            @RequestParam("available") Boolean available,
            @FromSession UserInfo userInfo) {
        return popularizationService.updateDocumentAvailable(documentId, available, userInfo.buildUserIdentifier());
    }

}
