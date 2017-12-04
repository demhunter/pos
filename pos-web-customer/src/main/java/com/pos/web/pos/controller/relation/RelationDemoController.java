/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.relation;

import com.pos.authority.service.support.relation.CustomerRelationNode;
import com.pos.authority.service.support.relation.CustomerRelationTree;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.common.util.mvc.support.ApiResult;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Stack;

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
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @RequestMapping(value = "initialize", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 初始化", notes = "初始化")
    public ApiResult<CustomerRelationTree> explain() {
        // customerRelationTreeSupport.initialize();
        return ApiResult.succ();
    }

    @RequestMapping(value = "participation/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取参与分佣的用户栈", notes = "成功返回，则栈顶一定为交易用户信息，栈底一定为根节点")
    public ApiResult<Stack<CustomerRelationNode>> brokerageParticipation(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        return ApiResult.succ(customerRelationPoolSupport.getParticipationForBrokerage(userId));
    }
}
