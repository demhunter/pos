/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.popularization;

import com.pos.basic.condition.orderby.PopularizationOrderField;
import com.pos.basic.condition.query.PopularizationCondition;
import com.pos.basic.dto.popularization.PopularizationDocumentDto;
import com.pos.basic.service.PopularizationService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文案推广相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/popularization")
@Api(value = "/popularization", description = "v2.0.0 * 下级相关接口")
public class PopularizationController {

    @Resource
    private PopularizationService popularizationService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取推广文案列表", notes = "获取推广文案列表")
    public ApiResult<Pagination<PopularizationDocumentDto>> getPopularization(
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        PopularizationCondition condition = new PopularizationCondition();
        condition.setAvailable(Boolean.TRUE);

        LimitHelper limitHelper = LimitHelper.create(pageNum, pageSize);

        return popularizationService.queryDocuments(
                condition, PopularizationOrderField.getDefaultOrderHelper(), limitHelper);
    }
}
