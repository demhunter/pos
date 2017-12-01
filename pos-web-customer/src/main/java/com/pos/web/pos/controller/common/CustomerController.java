/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.common;

import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.pos.constants.PosConstants;
import com.pos.pos.constants.UserAuditStatus;
import com.pos.pos.dto.auth.PosUserAuthDetailDto;
import com.pos.pos.service.PosService;
import com.pos.pos.service.PosUserTransactionRecordService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import com.pos.user.session.UserInfo;
import com.pos.web.pos.vo.response.CustomerVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 快捷收款用户信息相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
@RestController
@RequestMapping("/customer")
@Api(value = "/customer", description = "v2.0.0 用户信息接口")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @Resource
    private PosConstants posConstants;

    @Resource
    private PosService posService;

    @Resource
    private PosUserTransactionRecordService posUserTransactionRecordService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 获取用户信息，用以展示首页内容和我的页面的内容", notes = "获取用户信息，用以展示首页内容和我的页面的内容")
    public ApiResult<CustomerVo> getPosUserInfo(
            @FromSession UserInfo userInfo) {
        CustomerDto customer = customerService.findById(userInfo.getId(), true, false);
        if (customer == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        CustomerVo result = fillCustomerVo(customer);
        return ApiResult.succ(result);
    }

    private CustomerVo fillCustomerVo(CustomerDto customer) {
        CustomerVo result = new CustomerVo();
        customer.setHeadImage(posConstants.getPosHeadImage());
        customer.setNickName(StringUtils.isNotBlank(customer.getName()) ? customer.getName() : customer.getUserPhone());
        result.setCustomerDto(customer);
        PosUserAuthDetailDto authDetail =
                posService.findAuthDetail(customer.getId());
        if (authDetail != null) {
            result.setAuditStatus(authDetail.getAuditStatus());
            result.setRejectReason(authDetail.getRejectReason());

            UserAuditStatus auditStatus = authDetail.parseAuditStatus();
            // 没有交易记录的用户需要在快捷收款处显示小红点，引导用户点击
            result.setShowGetRedDot(posUserTransactionRecordService.queryUserTransactionCount(customer.getId()) <= 0);
        }

        return result;
    }
}
