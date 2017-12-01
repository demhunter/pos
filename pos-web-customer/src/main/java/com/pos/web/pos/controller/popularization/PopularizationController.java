/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.popularization;

import com.pos.basic.dto.popularization.PopularizationDocumentDto;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.Pagination;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文案推广相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@RestController
@RequestMapping("/descendant")
@Api(value = "/descendant", description = "v2.0.0 * 下级相关接口")
public class PopularizationController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取推广文案列表", notes = "获取推广文案列表")
    public ApiResult<Pagination<PopularizationDocumentDto>> getPopularization() {
        return null;
    }
}
