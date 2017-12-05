/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.customer;

import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.CustomerRelationService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.transaction.dto.brokerage.BrokerageGeneralInfoDto;
import com.pos.transaction.service.CustomerBrokerageService;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import com.pos.user.session.UserInfo;
import com.pos.web.pos.vo.response.CustomerVo;
import com.pos.web.pos.vo.response.RecommendSimpleVo;
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
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private CustomerRelationService customerRelationService;

    @Resource
    private CustomerBrokerageService customerBrokerageService;

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
        customer.setNickName(StringUtils.isNotBlank(customer.getName()) ? customer.getName() : customer.getUserPhone());
        result.setCustomerDto(customer);
        // 用户自身权限信息
        CustomerPermissionDto permission = customerAuthorityService.getPermission(customer.getId());
        result.setAuditStatus(permission.getAuditStatus());
        result.setRejectReason(permission.getRejectReason());
        result.setCurrentLevel(permission.getLevel());
        result.setCurrentLevelDesc("Lv" + permission.getLevel());
        // 用户累计收款和今日收益
        CustomerStatisticsDto statistics = customerStatisticsService.getStatistics(customer.getId());
        if (statistics != null) {
            result.setTotalWithdrawAmount(statistics.getWithdrawAmount());
        }
        BrokerageGeneralInfoDto general = customerBrokerageService.getBrokerageGeneral(customer.getId());
        result.setTodayBrokerage(general.getTodayBrokerage());
        // 用户的上级信息
        CustomerDto referrer = customerRelationService.getParentCustomer(customer.getId());
        if (referrer != null && !referrer.getId().equals(0L)) {
            RecommendSimpleVo recommendInfo = new RecommendSimpleVo();
            recommendInfo.setUserId(referrer.getId());
            recommendInfo.setName(referrer.getName());
            recommendInfo.setPhone(referrer.getUserPhone());

            result.setRecommendInfo(recommendInfo);
        }

        return result;
    }
}
