/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.posconsole.controller.customer;

import com.pos.authority.dto.permission.CustomerPermissionBasicDto;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
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
import com.pos.transaction.condition.orderby.PosUserOrderField;
import com.pos.transaction.condition.query.PosUserCondition;
import com.pos.transaction.constants.AuthStatusEnum;
import com.pos.transaction.constants.PosTwitterStatus;
import com.pos.transaction.dto.PosEnumsDto;
import com.pos.transaction.dto.PosUserAuditInfoDto;
import com.pos.transaction.dto.identity.IdentifyInfoDto;
import com.pos.transaction.dto.user.PosUserIntegrateDto;
import com.pos.transaction.service.PosService;
import com.pos.transaction.service.PosUserBrokerageRecordService;
import com.pos.transaction.service.PosUserService;
import com.pos.user.service.UserService;
import com.pos.user.session.UserInfo;
import com.pos.web.posconsole.converter.PosConverter;
import com.pos.web.posconsole.vo.pos.PosUserSimpleInfoVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
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
    private PosUserService posUserService;

    @Resource
    private PosUserBrokerageRecordService posUserBrokerageRecordService;

    @Resource
    private PosService posService;

    @RequestMapping(value = "enum", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 获取快捷收款相关枚举信息", notes = "获取快捷收款相关枚举信息")
    public ApiResult<PosEnumsDto> queryPosEnums() {
        return ApiResult.succ(PosEnumsDto.getInstance());
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
            @RequestParam(name = "searchType", required = false) Integer searchType,
            @ApiParam(name = "pageNum", value = "当前页编号")
            @RequestParam("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页显示的记录数量")
            @RequestParam("pageSize") int pageSize) {
        LimitHelper limitHelper = new LimitHelper(pageNum, pageSize);
        PosUserCondition condition = new PosUserCondition();
        condition.setUserAuditStatus(userAuditStatus);
        condition.setBeginTime(beginTime);
        condition.setEndTime(endTime);
        if (!StringUtils.isEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                return ApiResult.succ(Pagination.newInstance(limitHelper, 0));
            }
            condition.setIncludeUserIds(includeUserIds);
        }
        Pagination<PosUserIntegrateDto> pagination = posUserService.queryPosUsers(condition, PosUserOrderField.getDefaultOrderHelper(), limitHelper);
        Pagination<PosUserSimpleInfoVo> result = Pagination.newInstance(limitHelper, pagination.getTotalCount());
        if (pagination.getTotalCount() > 0 && !CollectionUtils.isEmpty(pagination.getResult())) {
            List<PosUserSimpleInfoVo> posUsers = pagination.getResult().stream()
                    .map(PosConverter::toPosUserSimpleInfoVo).collect(Collectors.toList());
            result.setResult(posUsers);
        }

        return ApiResult.succ(result);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取指定用户信息", notes = "获取指定用户信息")
    public ApiResult<Pagination<PosUserSimpleInfoVo>> getCustomer(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId) {
        return null;
    }

    @RequestMapping(value = "{userId}/available", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 启用或禁用用户", notes = "启用或禁用用户")
    public ApiResult<NullObject> updateUserAvailable(
            @ApiParam(name = "userId", value = "用户id")
            @PathVariable("userId") Long userId,
            @ApiParam(name = "available", value = "true：启用；false：禁用")
            @RequestParam("available") Boolean available,
            @FromSession UserInfo userInfo) {
        return null;
    }

    @RequestMapping(value = "{posId}/permission", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取快捷收款用户的权限信息", notes = "获取快捷收款用户的权限信息")
    public ApiResult<CustomerPermissionBasicDto> getPosUserPermission(
            @ApiParam(name = "posId", value = "快捷收款用户自增id")
            @PathVariable("posId") Long posId) {
        return null;//posUserService.getBaseAuthById(posId);
    }

    @RequestMapping(value = "{posId}/permission", method = RequestMethod.POST)
    @ApiOperation(value = "v2.0.0 * 更改快捷收款用户的权限信息", notes = "更改快捷收款用户的权限信息")
    public ApiResult<NullObject> updatePosUserPermission(
            @ApiParam(name = "posId", value = "快捷收款用户自增id")
            @PathVariable("posId") Long posId,
            @ApiParam(name = "permissionInfo", value = "新权限信息")
            @RequestBody CustomerPermissionBasicDto permissionInfo,
            @FromSession UserInfo userInfo) {
        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(posId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("更改快捷收款用户的权限信息时尝试获取锁失败！posId = " + posId, e);
        }
        if (hasLock) {
            try {
                return null;//posUserService.updatePosUserAuth(posId, permissionInfo, userInfo.buildUserIdentifier());
            } finally {
                lock.unlock();
            }
        } else {
            return ApiResult.fail(CommonErrorCode.ACCESS_TIMEOUT);
        }

    }

    @RequestMapping(value = "{posId}/audit", method = RequestMethod.GET)
    @ApiOperation(value = "v2.0.0 * 获取被审核的用户信息", notes = "获取被审核的用户信息")
    public ApiResult<PosUserAuditInfoDto> getAuditInfo(
            @ApiParam(name = "posId", value = "快捷收款用户自增id")
            @PathVariable("posId") Long posId) {
        return posService.getAuditInfo(posId, true);
    }

    @RequestMapping(value = "{posId}/audit", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 审核用户信息", notes = "审核用户信息")
    public ApiResult<NullObject> auditPosUserInfo(
            @ApiParam(name = "posId", value = "被审核的快捷收款用户自增id")
            @PathVariable("posId") Long posId,
            @ApiParam(name = "allowed", value = "是否通过审核（true：通过审核，false：不通过审核）")
            @RequestParam("allowed") Boolean allowed,
            @ApiParam(name = "updateKey", value = "更新Key，通过比对此字段校验审核数据是否有变动，在审核时需要回传此字段（时间戳格式）")
            @RequestParam("updateKey") String updateKey,
            @ApiParam(name = "rejectReason", value = "审核不通过原因（当allowed = false时，此字段必填）")
            @RequestParam(name = "rejectReason", required = false) String rejectReason,
            @FromSession UserInfo userInfo) {
        IdentifyInfoDto identifyInfo = new IdentifyInfoDto();
        identifyInfo.setPosAuthId(posId);
        identifyInfo.setAllowed(allowed);
        identifyInfo.setRejectReason(rejectReason);
        identifyInfo.setOperatorUserId(userInfo.getId());
        identifyInfo.setUpdateKey(updateKey);

        boolean hasLock = false;
        ReentrantLock lock = SEG_LOCKS.getLock(posId);
        try {
            hasLock = lock.tryLock(8L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("审核用户信息时尝试获取锁失败！posId = " + posId, e);
        }
        if (hasLock) {
            try {
                return posService.identifyPosUserInfo(identifyInfo);
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
            @ApiParam(name = "bindingCard", value = "是否与有绑定银行卡（true：已绑定，false：未绑定，null：不限）")
            @RequestParam(name = "bindingCard", required = false) Boolean bindingCard,
            @ApiParam(name = "getPermission", value = "是否有收款权限（true：启用，false：禁用，null：不限）")
            @RequestParam(name = "getPermission", required = false) Boolean getPermission,
            @ApiParam(name = "isTwitter", value = "是否是推客（true：是推客，false：不是推客，null：不限）")
            @RequestParam(name = "isTwitter", required = false) Boolean isTwitter,
            @ApiParam(name = "withdrawDeposit", value = "是否存在提现申请（true：是，false：否，null：不限）")
            @RequestParam(name = "withdrawDeposit", required = false) Boolean withdrawDeposit,
            @ApiParam(name = "beginTime", value = "注册开始时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "beginTime", required = false) String beginTime,
            @ApiParam(name = "endTime", value = "注册结束时间（格式：yyyy-MM-dd）")
            @RequestParam(name = "endTime", required = false) String endTime,
            @ApiParam(name = "searchKey", value = "搜索关键字（手机号/姓名）")
            @RequestParam(name = "searchKey", required = false) String searchKey) {
        LimitHelper limitHelper = new LimitHelper(1, Integer.MAX_VALUE, false);
        PosUserCondition condition = new PosUserCondition();
        condition.setUserAuditStatus(userAuditStatus);
        condition.setBindingCard(bindingCard);
        condition.setGetPermission(getPermission);
        condition.setTwitterPermission(isTwitter);
        condition.setWithdrawDeposit(withdrawDeposit);
        condition.setBeginTime(beginTime);
        condition.setEndTime(endTime);
        XlsView xlsView;
        if (!StringUtils.isEmpty(searchKey)) {
            List<Long> includeUserIds = userService.queryUserIds(searchKey);
            if (CollectionUtils.isEmpty(includeUserIds)) {
                xlsView = new XlsView(0, new String[]
                        {"手机号", "姓名", "注册快捷收款时间", "身份认证信息", "收款银行卡",
                                "收款银行卡信息", "收款手续费率", "收款笔数/金额", "快捷收款权限",
                                "是否是推客", "推客权限", "存在提现申请"})
                        .setXlsStyle(new XlsStyle().setSheetName("快捷收款用户列表").setColumnWidth(17));
                return new ModelAndView(xlsView);
            }
            condition.setIncludeUserIds(includeUserIds);
        }
        Pagination<PosUserIntegrateDto> pagination = posUserService.queryPosUsers(condition, PosUserOrderField.getDefaultOrderHelper(), limitHelper);
        xlsView = new XlsView(pagination.getTotalCount(), new String[]{
                "手机号",
                "姓名",
                "注册快捷收款时间",
                "身份认证信息",
                "收款银行卡",
                "收款银行卡信息",
                "收款手续费率",
                "收款笔数/金额",
                "快捷收款权限",
                "是否是推客",
                "推客权限",
                "存在提现申请"
        }).setXlsStyle(new XlsStyle().setSheetName("快捷收款用户列表").setColumnWidth(17));
        if (pagination.getTotalCount() > 0 && !CollectionUtils.isEmpty(pagination.getResult())) {
            List<PosUserSimpleInfoVo> posUsers = pagination.getResult().stream()
                    .map(PosConverter::toPosUserSimpleInfoVo).collect(Collectors.toList());
            posUsers.forEach(posUser ->
                    xlsView.addRowValues(new Object[]{
                            posUser.getPhone(),
                            posUser.getName(),
                            SimpleDateUtils.formatDate(posUser.getRegisterTime(), SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString()),
                            posUser.getUserAuditStatusDesc(),
                            posUser.getBindingCard() ? "已绑定" : "未绑定",
                            posUser.getBindingCard() ? posUser.getBankName() + "（" + posUser.getCardNo() + "）" : "",
                            posUser.getBaseAuth().getGetRate().multiply(new BigDecimal(100)) + "% + " + posUser.getBaseAuth().getPoundage() + "元",
                            posUser.getUserPosCount() + " / " + posUser.getUserPosAmount(),
                            AuthStatusEnum.ENABLE.equals(posUser.getBaseAuth().parseGetAuth()) ? "启用" : "禁用",
                            PosTwitterStatus.ENABLE.equals(posUser.getBaseAuth().parseTwitterStatus()) ? "是" : "否",
                            AuthStatusEnum.ENABLE.equals(posUser.getBaseAuth().parseDevelopAuth()) ?
                                    "发展下级推客\n" + (AuthStatusEnum.ENABLE.equals(posUser.getBaseAuth().parseSpreadAuth()) ?
                                            "发展收款客户" : "")
                                    : "",
                            posUser.getWithdrawDepositApply() ? "是（" + posUser.getWithdrawDepositAmount() + "）" : "否"
                    }));
        }
        return new ModelAndView(xlsView);
    }
}
