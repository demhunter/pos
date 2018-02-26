/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.customer;

import com.pos.authority.service.CustomerRelationService;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.authority.service.support.relation.CustomerRelationTree;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.user.session.UserInfo;
import com.pos.web.console.vo.relation.RelationSimpleVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 客户关系相关接口
 *
 * @author wangbing
 * @version 1.0, 2018/2/26
 */
@RestController
@RequestMapping("/customer/relation")
@Api(value = "/customer/relation", description = "v2.0.4 * 客户关系相关接口")
public class CustomerRelationController {

    @Resource
    private CustomerRelationService customerRelationService;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @RequestMapping(value = "modify", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.4 * 客户关系修改", notes = "客户关系修改")
    public ApiResult<NullObject> modifyCustomerRelation(
            @ApiParam(name = "relationVo", value = "关系信息Vo")
            @RequestBody RelationSimpleVo relationVo,
            @FromSession UserInfo userInfo) {
        return customerRelationService.modifyCustomerRelation(
                relationVo.getChildUserId(), relationVo.getParentUserId(), userInfo.buildUserIdentifier());
    }

    @RequestMapping(value = "tree", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.4 * 完整关系树", notes = "完整关系树")
    public ApiResult<CustomerRelationTree> getWholeRelationTree() {

        return ApiResult.succ(customerRelationPoolSupport.getCustomerRelationTree(0L));
    }
}
