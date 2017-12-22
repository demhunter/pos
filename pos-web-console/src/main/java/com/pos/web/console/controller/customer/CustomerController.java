/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.customer;

import com.google.common.collect.Lists;
import com.pos.authority.condition.oerderby.CustomerIntegrateOrderField;
import com.pos.authority.condition.query.CustomerIntegrateCondition;
import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.converter.CustomerPermissionConverter;
import com.pos.authority.dto.CustomerEnumsDto;
import com.pos.authority.dto.customer.CustomerIntegrateInfoDto;
import com.pos.authority.dto.identity.CustomerIdentityDto;
import com.pos.authority.dto.identity.IdentifyInfoDto;
import com.pos.authority.dto.permission.CustomerPermissionBasicDto;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.statistics.DescendantStatisticsDto;
import com.pos.authority.exception.AuthorityErrorCode;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.common.util.basic.SegmentLocks;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.mvc.view.XlsStyle;
import com.pos.common.util.mvc.view.XlsView;
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.service.PosCardService;
import com.pos.transaction.service.PosService;
import com.pos.transaction.service.PosUserBrokerageRecordService;
import com.pos.transaction.service.PosUserService;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.UserService;
import com.pos.user.session.UserInfo;
import com.pos.web.console.converter.PosConverter;
import com.pos.web.console.vo.audit.CustomerAuditInfoVo;
import com.pos.web.console.vo.pos.PosUserSimpleInfoVo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 快捷收款用户相关Controller
 *
 * @author wangbing
 * @version 1.0, 2017/8/24
 */
@RestController
@RequestMapping("/customer")
@Api(value = "/customer", description = "v2.0.0 快捷收款用户相关接口(接口路径有变化)")
public class CustomerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final static SegmentLocks SEG_LOCKS = new SegmentLocks(8, false);

    @Resource
    private UserService userService;

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private PosCardService posCardService;

    @RequestMapping(value = "enum", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取用户相关枚举信息", notes = "获取用户相关枚举信息")
    public ApiResult<CustomerEnumsDto> queryPosEnums() {
        return customerAuthorityService.queryPosCustomerEnums();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 快捷收款用户列表", notes = "快捷收款用户列表")
    public ApiResult<Pagination<PosUserSimpleInfoVo>> queryCustomers(
            @ApiParam(name = "userAuditStatus", value = "身份认证状态（0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过，null：不限）")
            @RequestParam(name = "userAuditStatus", required = false) Integer userAuditStatus,
            @ApiParam(name = "level", value = "用户等级（1 = Lv1，2 = Lv2，3 = Lv3，4：Lv4）")
            @RequestParam(name = "level", required = false) Integer level,
            @ApiParam(name = "userAvailable", value = "账号状态（true：启用，false：禁用）")
            @RequestParam(name = "userAvailable", required = false) Boolean userAvailable,
            @ApiParam(name = "existedInterview", value = "是否有回访记录（true：有，false：没有）")
            @RequestParam(name = "existedInterview", required = false) Boolean existedInterview,
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "搜索关键字（手机号/姓名）")
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "searchType", value = "搜索关键字类型（1：查询用户本人；2：查询直接下级（默认为1））")
            @RequestParam(name = "searchType", required = false, defaultValue = "1") Integer searchType,
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        LimitHelper limitHelper = new LimitHelper(pageNum, pageSize);
        CustomerIntegrateCondition condition = new CustomerIntegrateCondition();
        condition.setAuditStatus(userAuditStatus);
        condition.setLevel(level);
        condition.setEnable(userAvailable);
        condition.setInterviewed(existedInterview);
        condition.setBeginTime(beginTime);
        condition.setEndTime(endTime);
        if (!StringUtils.isEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                return ApiResult.succ(Pagination.newInstance(limitHelper, 0));
            }
            condition.setIncludeUserIds(includeUserIds);
            condition.setIncludeUserIdsType(searchType);
        }

        Pagination<CustomerIntegrateInfoDto> pagination = customerAuthorityService.queryCustomerIntegrates(
                condition, CustomerIntegrateOrderField.getDefaultOrderHelper(), limitHelper);
        Pagination<PosUserSimpleInfoVo> result = Pagination.newInstance(limitHelper, pagination.getTotalCount());
        if (pagination.getTotalCount() > 0 && !CollectionUtils.isEmpty(pagination.getResult())) {
            List<PosUserSimpleInfoVo> posUsers = pagination.getResult().stream()
                    .map(PosConverter::toPosUserSimpleInfoVo).collect(Collectors.toList());
            // 填充银行卡信息和间接下级统计
            fillCardInfoAndDescendantStatistics(posUsers);

            result.setResult(posUsers);
        }

        return ApiResult.succ(result);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取指定用户信息", notes = "获取指定用户信息")
    public ApiResult<PosUserSimpleInfoVo> getCustomer(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        CustomerIntegrateInfoDto integrateInfoDto = customerAuthorityService.findCustomerIntegrate(userId);
        if (integrateInfoDto == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        PosUserSimpleInfoVo result = PosConverter.toPosUserSimpleInfoVo(integrateInfoDto);

        if (result.getBindingCard()) {
            PosCardDto card = posCardService.queryBankCards(Lists.newArrayList(result.getPosCardId()), true).get(result.getPosCardId());
            if (card != null) {
                result.setBankName(card.getBankName());
                result.setCardNo(card.getCardNO());
            }
        }
        DescendantStatisticsDto descendantStatistics = customerStatisticsService.getDescendantStatistics(userId);
        result.setDescendantCount(descendantStatistics.getDescendantCount());

        return ApiResult.succ(result);
    }

    @RequestMapping(value = "{userId}/available", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 启用或禁用用户", notes = "启用或禁用用户")
    public ApiResult<NullObject> updateUserAvailable(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId,
            @ApiParam(name = "available", value = "true：启用；false：禁用")
            @RequestParam("available") Boolean available,
            @FromSession UserInfo userInfo) {
        return customerAuthorityService.updateUserAvailable(userId, available, userInfo.buildUserIdentifier());
    }

    @RequestMapping(value = "{userId}/permission", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取快捷收款用户的权限信息", notes = "获取快捷收款用户的权限信息")
    public ApiResult<CustomerPermissionBasicDto> getPosUserPermission(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        CustomerPermissionBasicDto result = customerAuthorityService.getPermissionBasicInfo(userId);
        result.hundredPercentRate();
        return ApiResult.succ(result);
    }

    @RequestMapping(value = "{userId}/permission", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 更改快捷收款用户的权限信息", notes = "更改快捷收款用户的权限信息")
    public ApiResult<NullObject> updatePosUserPermission(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId,
            @ApiParam(name = "permissionInfo", value = "新权限信息")
            @RequestBody CustomerPermissionBasicDto permissionInfo,
            @FromSession UserInfo userInfo) {
        permissionInfo.tenThousandPercentRate();
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(userId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("更改快捷收款用户的权限信息时尝试获取锁失败！userId = " + userId, e);
        }
        if (hasLock) {
            try {
                return customerAuthorityService.updatePermission(permissionInfo, userInfo.buildUserIdentifier());
            } finally {
                lock.unlock();
            }
        } else {
            return ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
        }

    }

    @RequestMapping(value = "{userId}/audit", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取被审核的用户信息", notes = "获取被审核的用户信息")
    public ApiResult<CustomerAuditInfoVo> getAuditInfo(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {

        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        if (permission == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (CustomerAuditStatus.NOT_SUBMIT.equals(permission.parseAuditStatus())) {
            return ApiResult.fail(AuthorityErrorCode.AUDIT_STATUS_ERROR_NOU_SUBMIT_FOR_AUTHORIZE);
        }

        CustomerAuditInfoVo result = new CustomerAuditInfoVo();
        result.setUpdateKey(permission.getUpdateTime());
        if (CustomerAuditStatus.REJECTED.equals(permission.parseAuditStatus())) {
            result.setRejectReason(permission.getRejectReason());
        }

        CustomerIdentityDto identityInfo = CustomerPermissionConverter.buildCustomerIdentity(permission);
        customerAuthorityService.decryptedCustomerIdentity(identityInfo);
        result.setIdentityInfo(identityInfo);

        result.setBindCardInfo(posCardService.getWithdrawCard(userId, true));

        return ApiResult.succ(result);
    }

    @RequestMapping(value = "{userId}/audit", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 审核用户信息", notes = "审核用户信息")
    public ApiResult<NullObject> auditPosUserInfo(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId,
            @ApiParam(name = "allowed", value = "是否通过审核（true：通过审核，false：不通过审核）")
            @RequestParam("allowed") Boolean allowed,
            @ApiParam(name = "updateKey", value = "更新Key，通过比对此字段校验审核数据是否有变动，在审核时需要回传此字段（时间戳格式）")
            @RequestParam("updateKey") String updateKey,
            @ApiParam(name = "rejectReason", value = "审核不通过原因（当allowed = false时，此字段必填）")
            @RequestParam(name = "rejectReason", required = false) String rejectReason,
            @FromSession UserInfo userInfo) {
        IdentifyInfoDto identifyInfo = new IdentifyInfoDto();
        identifyInfo.setUserId(userId);
        identifyInfo.setAllowed(allowed);
        identifyInfo.setRejectReason(rejectReason);
        identifyInfo.setOperatorUserId(userInfo.getId());
        identifyInfo.setUpdateKey(updateKey);

        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(userId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("审核用户信息时尝试获取锁失败！posId = " + userId, e);
        }
        if (hasLock) {
            try {
                return customerAuthorityService.identifyPosUserInfo(identifyInfo);
            } finally {
                lock.unlock();
            }
        } else {
            return ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
        }
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 导出符合条件的快捷收款用户列表", notes = "导出符合条件的快捷收款用户列表" +
            "（ps：该接口前端不处理返回结果，导出成功后浏览器将自动下载Excel文件）")
    public ModelAndView findAndExportPosUsers(
            @ApiParam(name = "userAuditStatus", value = "身份认证状态（0 = 未提交，1 = 未审核，2 = 已通过，3 = 未通过，null：不限）")
            @RequestParam(name = "userAuditStatus", required = false) Integer userAuditStatus,
            @ApiParam(name = "level", value = "用户等级（1 = Lv1，2 = Lv2，3 = Lv3，4：Lv4）")
            @RequestParam(name = "level", required = false) Integer level,
            @ApiParam(name = "userAvailable", value = "账号状态（true：启用，false：禁用）")
            @RequestParam(name = "userAvailable", required = false) Boolean userAvailable,
            @ApiParam(name = "existedInterview", value = "是否有回访记录（true：有，false：没有）")
            @RequestParam(name = "existedInterview", required = false) Boolean existedInterview,
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "搜索关键字（手机号/姓名）")
            @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "searchType", value = "搜索关键字类型（1：查询用户本人；2：查询直接下级（默认为1））")
            @RequestParam(name = "searchType", required = false) Integer searchType) {
        XlsView xlsView;
        LimitHelper limitHelper = new LimitHelper(1, Integer.MAX_VALUE, false);
        CustomerIntegrateCondition condition = new CustomerIntegrateCondition();
        condition.setAuditStatus(userAuditStatus);
        condition.setLevel(level);
        condition.setEnable(userAvailable);
        condition.setInterviewed(existedInterview);
        condition.setBeginTime(beginTime);
        condition.setEndTime(endTime);
        if (!StringUtils.isEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                xlsView = new XlsView(0, getRawNames())
                        .setXlsStyle(new XlsStyle().setSheetName("快捷收款用户列表").setColumnWidth(20));
                return new ModelAndView(xlsView);
            }
            condition.setIncludeUserIds(includeUserIds);
            condition.setIncludeUserIdsType(searchType);
        }

        Pagination<CustomerIntegrateInfoDto> pagination = customerAuthorityService.queryCustomerIntegrates(
                condition, CustomerIntegrateOrderField.getDefaultOrderHelper(), limitHelper);
        xlsView = new XlsView(pagination.getTotalCount(), getRawNames())
                .setXlsStyle(new XlsStyle().setSheetName("快捷收款用户列表").setColumnWidth(20));
        if (pagination.getTotalCount() > 0 && !CollectionUtils.isEmpty(pagination.getResult())) {
            List<PosUserSimpleInfoVo> posUsers = pagination.getResult().stream()
                    .map(PosConverter::toPosUserSimpleInfoVo).collect(Collectors.toList());
            // 填充银行卡信息和间接下级统计
            fillCardInfoAndDescendantStatistics(posUsers);

            posUsers.forEach(posUser -> xlsView.addRowValues(getRawValues(posUser)));
        }

        return new ModelAndView(xlsView);
    }

    private String[] getRawNames() {
        return new String[]{
                "姓名",
                "手机号",
                "注册时间",
                "上级姓名",
                "上级手机号",
                "实名认证",
                "结算银行名称",
                "结算银行卡卡号",
                "用户等级",
                "收款手续费率",
                "收款笔数",
                "收款金额",
                "直接下级",
                "间接下级",
                "可提现佣金",
                "佣金提现次数",
                "已提现佣金金额",
                "状态",
                "回访次数"
        };
    }

    private Object[] getRawValues(PosUserSimpleInfoVo posUser) {
        return new Object[]{
                posUser.getName(),
                posUser.getPhone(),
                SimpleDateUtils.formatDate(posUser.getRegisterTime(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                posUser.getExistedParent() ? posUser.getParentName() : "-",
                posUser.getExistedParent() ? posUser.getParentPhone() : "-",
                posUser.getUserAuditStatusDesc(),
                posUser.getBindingCard() ? posUser.getBankName() : "-",
                posUser.getBindingCard() ? posUser.getCardNo() : "-",
                "Lv" + posUser.getLevel(),
                posUser.getWithdrawRate() + "% + " + posUser.getExtraServiceCharge() + "元",
                posUser.getUserPosCount(),
                posUser.getUserPosAmount(),
                posUser.getChildrenCount(),
                posUser.getDescendantCount(),
                posUser.getCurrentBrokerage(),
                posUser.getBrokerageAppliedCount(),
                posUser.getAppliedBrokerage(),
                posUser.getUserAvailable() ? "启用" : "禁用",
                posUser.getInterviewCount()
        };
    }

    // 填充银行卡信息和间接下级统计
    private void fillCardInfoAndDescendantStatistics(List<PosUserSimpleInfoVo> posUsers) {
        List<Long> posCardIds = posUsers.stream()
                .filter(PosUserSimpleInfoVo::getBindingCard)
                .map(PosUserSimpleInfoVo::getPosCardId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(posCardIds)) {
            Map<Long, PosCardDto> cardMap = posCardService.queryBankCards(posCardIds, true);
            if (!CollectionUtils.isEmpty(cardMap)) {
                posUsers.forEach(e -> {
                    DescendantStatisticsDto descendantStatistics = customerStatisticsService.getDescendantStatistics(e.getUserId());
                    e.setDescendantCount(descendantStatistics.getDescendantCount());
                    if (e.getBindingCard()) {
                        PosCardDto card = cardMap.get(e.getPosCardId());
                        if (card != null) {
                            e.setBankName(card.getBankName());
                            e.setCardNo(card.getCardNO());
                        }
                    }
                });
            }
        }

        posUsers.forEach(e -> {
            DescendantStatisticsDto descendantStatistics = customerStatisticsService.getDescendantStatistics(e.getUserId());
            e.setDescendantCount(descendantStatistics.getDescendantCount());
        });
    }
}
