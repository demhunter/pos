/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.relation;

import com.pos.authority.dto.relation.CustomerRelationTree;
import com.pos.authority.service.support.CustomerRelationTreeSupport;
import com.pos.common.util.mvc.support.ApiResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 关系DemoController
 *
 * @author wangbing
 * @version 1.0, 2017/11/24
 */
@RestController
@RequestMapping("/relation")
@Api(value = "/relation", description = "v2.0.0 * 关系demo接口")
public class RelationDemoController {

    @Resource
    private CustomerRelationTreeSupport customerRelationTreeSupport;

    @RequestMapping(value = "initialize", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化", notes = "初始化")
    public ApiResult<CustomerRelationTree> explain() {
        return ApiResult.succ(customerRelationTreeSupport.initializeRelationTree());
    }
}
