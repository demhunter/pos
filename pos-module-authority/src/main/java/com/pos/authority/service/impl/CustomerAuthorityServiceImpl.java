/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.google.common.collect.Lists;
import com.pos.authority.condition.oerderby.CustomerIntegrateOrderField;
import com.pos.authority.condition.query.CustomerIntegrateCondition;
import com.pos.authority.constant.AuthorityConstants;
import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.converter.CustomerLevelConverter;
import com.pos.authority.converter.CustomerPermissionConverter;
import com.pos.authority.dao.CustomerIntegrateDao;
import com.pos.authority.dao.CustomerPermissionDao;
import com.pos.authority.dao.CustomerRelationDao;
import com.pos.authority.dao.CustomerStatisticsDao;
import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.domain.CustomerRelation;
import com.pos.authority.domain.CustomerStatistics;
import com.pos.authority.dto.CustomerEnumsDto;
import com.pos.authority.dto.customer.CustomerIntegrateInfoDto;
import com.pos.authority.dto.identity.CustomerIdentityDto;
import com.pos.authority.dto.identity.IdentifyInfoDto;
import com.pos.authority.dto.level.CustomerLevelConfigDto;
import com.pos.authority.dto.level.CustomerUpgradeLevelDto;
import com.pos.authority.dto.permission.CustomerPermissionBasicDto;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.dto.statistics.CustomerStatisticsDto;
import com.pos.authority.exception.AuthorityErrorCode;
import com.pos.authority.fsm.AuthorityFSMFactory;
import com.pos.authority.fsm.context.AuditStatusTransferContext;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.authority.service.support.CustomerLevelSupport;
import com.pos.authority.service.support.CustomerRelationPoolSupport;
import com.pos.basic.dto.CommonEnumDto;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.basic.sm.fsm.FSM;
import com.pos.common.util.mvc.support.*;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.constant.UserType;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import com.pos.user.session.UserInfo;
import com.pos.user.session.UserSessionComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 客户权限ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/11/30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerAuthorityServiceImpl implements CustomerAuthorityService {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerAuthorityServiceImpl.class);

    @Resource
    private CustomerService customerService;

    @Resource
    private SecurityService securityService;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private CustomerLevelSupport customerLevelSupport;

    @Resource
    private CustomerRelationPoolSupport customerRelationPoolSupport;

    @Resource
    private CustomerPermissionDao customerPermissionDao;

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Resource
    private CustomerStatisticsDao customerStatisticsDao;

    @Resource
    private CustomerIntegrateDao customerIntegrateDao;

    @Resource
    private AuthorityConstants authorityConstants;

    @Resource
    private UserSessionComponent userSessionComponent;

    @Override
    public void initialize(Long userId, Long parentUserId) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerDto newCustomer = customerService.findById(userId, true, true);
        if (newCustomer == null) {
            LOG.error("客户{}不存在，无法初始化权限和客户关系", userId);
        } else if (!newCustomer.isAvailable()) {
            LOG.error("客户{}已被禁用，无法初始化权限和客户关系", userId);
        } else if (newCustomer.isDeleted()) {
            LOG.error("客户{}已被删除，无法初始化权限和客户关系", userId);
        } else {
            // 初始化客户收款权限并保存
            CustomerLevelConfig minLevelConfig = customerLevelSupport.getMinLevelConfig();
            CustomerPermission permission = new CustomerPermission(userId, minLevelConfig);
            customerPermissionDao.save(permission);

            // 初始化客户统计信息并保存
            CustomerStatistics statistics = new CustomerStatistics(userId);
            customerStatisticsDao.save(statistics);

            // 初始化客户关系信息
            CustomerDto parentCustomer = null;
            if (parentUserId != null) {
                parentCustomer = customerService.findById(parentUserId, false, false);
            }
            if (parentCustomer == null) {
                LOG.warn("客户{}的上级客户{}不存在", userId, parentUserId);
                parentUserId = 0L;
            }
            CustomerRelation relation = new CustomerRelation(userId, parentUserId);
            customerRelationDao.save(relation);

            // 往客户关系池中添加关系
            customerRelationPoolSupport.addCustomerRelation(buildRelationDto(permission, relation));

            // 更新父客户的直接下级客户数量统计
            customerStatisticsService.incrementChildrenCount(parentUserId);
        }
    }

    private CustomerRelationDto buildRelationDto(CustomerPermission permission, CustomerRelation relation) {
        CustomerRelationDto relationDto = new CustomerRelationDto();
        relationDto.setId(relation.getId());
        relationDto.setUserId(relation.getUserId());
        relationDto.setLevel(permission.getLevel());
        relationDto.setWithdrawRate(permission.getWithdrawRate());
        relationDto.setExtraServiceCharge(permission.getExtraServiceCharge());
        relationDto.setAuditStatus(permission.getAuditStatus());
        relationDto.setParentUserId(relation.getParentUserId());
        relationDto.setRemark(relation.getRemark());
        relationDto.setRelationTime(new Date());

        return relationDto;
    }

    @Override
    public CustomerPermissionDto getPermission(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerPermission permission = customerPermissionDao.getPermission(userId);
        if (permission == null) {
            LOG.error("客户{}的权限信息不存在！", userId);
            throw new IllegalStateException("客户" + userId + "的权限信息不存在！");
        }

        return CustomerPermissionConverter.toCustomerPermissionDto(permission);
    }

    @Override
    public CustomerPermissionBasicDto getPermissionBasicInfo(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerPermission permission = customerPermissionDao.getPermission(userId);
        if (permission == null) {
            LOG.error("客户{}的权限信息不存在！", userId);
            throw new IllegalStateException("客户" + userId + "的权限信息不存在！");
        }

        return CustomerPermissionConverter.toCustomerPermissionBasicDto(permission);
    }

    @Override
    public ApiResult<NullObject> updatePermission(CustomerPermissionBasicDto basicPermission, UserIdentifier operator) {
        FieldChecker.checkEmpty(basicPermission, "basicPermission");
        FieldChecker.checkEmpty(operator, "operator");
        basicPermission.check("basicPermission");
        operator.check("operator");

        CustomerPermissionDto permission = getPermission(basicPermission.getUserId());
        if (basicPermission.getLevel().compareTo(permission.getLevel()) < 0) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_LEVEL_LESS_THAN_CURRENT);
        }

        CustomerLevelConfig maxLevelConfig = customerLevelSupport.getMaxLevelConfig();
        if (basicPermission.getLevel().compareTo(maxLevelConfig.getLevel()) > 0) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_GREATER_THAN_MAX);
        }

        if (basicPermission.getWithdrawRate().compareTo(authorityConstants.getPosWithdrawRateDownLimit()) < 0) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_RATE_LESS_THAN_LIMIT);
        }

        if (basicPermission.getExtraServiceCharge().compareTo(authorityConstants.getPosExtraServiceChargeDownLimit()) < 0) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_EXTRA_LESS_THAN_LIMIT);
        }

        customerPermissionDao.updateLevelConfig(permission);
        customerRelationPoolSupport.updateLevelConfig(permission);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<List<CustomerLevelConfigDto>> getLevels(Long userId) {

        final CustomerRelationDto customerRelation = userId == null ?
                null : customerRelationPoolSupport.getCustomerRelation(userId);

        Set<Integer> levelSet = customerLevelSupport.getLevels();
        List<CustomerLevelConfigDto> levelConfigs = Lists.newArrayList();

        levelSet.forEach(level -> {
            CustomerLevelConfigDto config = CustomerLevelConverter
                    .toCustomerLevelConfigDto(customerLevelSupport.getLevelConfig(level));
            if (customerRelation != null && customerRelation.getLevel().equals(config.getLevel())) {
                config.setCustomerCurrentLevel(Boolean.TRUE);
                config.setWithdrawRate(customerRelation.getWithdrawRate());
                config.setExtraServiceCharge(customerRelation.getExtraServiceCharge());
            }
            config.calculateBrokerage(authorityConstants.getPosWithdrawBasicRate());
            levelConfigs.add(config);
        });
        Collections.sort(levelConfigs);

        return ApiResult.succ(levelConfigs);
    }

    @Override
    public ApiResult<CustomerUpgradeLevelDto> getCustomerUpgradeInfo(Long userId, Integer targetLevel) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(targetLevel, "targetLevel");

        CustomerRelationDto currentInfo = customerRelationPoolSupport.getCustomerRelation(userId);
        if (currentInfo == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        // 校验用户当前等级是否已达到或超过目标等级
        if (currentInfo.getLevel().compareTo(targetLevel) >= 0) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_TARGET_IS_ARRIVED);
        }
        // 校验目标等级是否可以通过普通升级达到
        CustomerLevelConfigDto targetConfig = CustomerLevelConverter
                .toCustomerLevelConfigDto(customerLevelSupport.getLevelConfig(targetLevel));
        if (!targetConfig.getCanUpgrade()) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_TARGET_IS_UNREACHABLE);
        }
        // 查询用户晋升等级已支付的服务费
        CustomerStatisticsDto statistics = customerStatisticsDao.getByUserId(userId);

        CustomerUpgradeLevelDto result = new CustomerUpgradeLevelDto(userId, targetLevel);
        result.setCurrentLevel(currentInfo.getLevel());
        result.setWithdrawRate(currentInfo.getWithdrawRate());
        result.setExtraServiceCharge(currentInfo.getExtraServiceCharge());
        if (statistics != null) {
            result.setChildrenCount(statistics.getChildrenCount());
            result.setPaidCharge(statistics.getPaidCharge());
            result.setTotalWithdrawAmount(statistics.getWithdrawAmount());
        }
        result.setLevelPriceDifference(targetConfig.getChargeLimit().subtract(result.getPaidCharge()));

        return ApiResult.succ(result);
    }

    @Override
    public CustomerIdentityDto getCustomerIdentity(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerPermissionDto permission = getPermission(userId);

        return CustomerPermissionConverter.buildCustomerIdentity(permission);
    }

    @Override
    public void decryptedCustomerIdentity(CustomerIdentityDto identity) {
        FieldChecker.checkEmpty(identity, "identity");

        identity.setRealName(securityService.decryptData(identity.getRealName()));
        identity.setIdCardNo(securityService.decryptData(identity.getIdCardNo()));
    }

    @Override
    public ApiResult<NullObject> updateCustomerIdentity(Long userId, CustomerIdentityDto identity) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(identity, "identity");
        identity.check("identity", securityService);

        CustomerPermissionDto permission = getPermission(userId);

        // 已审核和待审核状态不允许更新实名信息，审核未通过的只允许修改身份证正反面图片
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.AUDITED.equals(auditStatus)) {
            return ApiResult.fail(AuthorityErrorCode.AUDIT_STATUS_ERROR_AUDITED_FOR_UPDATE);
        } else if (CustomerAuditStatus.NOT_AUDIT.equals(auditStatus)) {
            return ApiResult.fail(AuthorityErrorCode.AUDIT_STATUS_ERROR_NOT_AUDIT_FOR_UPDATE);
        } else if (CustomerAuditStatus.NOT_SUBMIT.equals(auditStatus)) {
            // 未提交状态设置姓名和身份证号
            permission.setIdCardName(identity.getRealName());
            permission.setIdCardNo(identity.getIdCardNo());
        }
        permission.setIdImageA(identity.getIdImageA());
        permission.setIdImageB(identity.getIdImageB());
        permission.setUpdateUserId(userId);

        customerPermissionDao.updateCustomerIdentity(permission);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> updateWithdrawCard(CustomerPermissionDto permission) {
        FieldChecker.checkEmpty(permission, "permission");
        FieldChecker.checkEmpty(permission.getUserId(), "permission.userId");

        customerPermissionDao.updateWithdrawCard(permission);

        return ApiResult.succ();
    }

    @Override
    public boolean updateAuditStatus(AuditStatusTransferContext transferContext, CustomerAuditStatus auditStatus) {
        // 参数校验
        FieldChecker.checkEmpty(transferContext, "transferContext");
        FieldChecker.checkEmpty(auditStatus, "auditStatus");
        FieldChecker.checkEmpty(transferContext.getPosAuthId(), "posAuthId");
        FieldChecker.checkEmpty(transferContext.getOperatorUserId(), "operatorUserId");
        if (CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            FieldChecker.checkEmpty(transferContext.getRejectReason(), "rejectReason");
        }

        customerPermissionDao.updateAuditStatus(transferContext.getUserId(), auditStatus.getCode(),
                transferContext.getRejectReason(), transferContext.getOperatorUserId());

        customerRelationPoolSupport.updateAuditStatus(transferContext.getUserId(), auditStatus);

        return true;
    }

    @Override
    public void upgradeLevel(CustomerPermissionDto permission, CustomerLevelConfig targetLevelConfig, Long operatorUserId) {
        permission.setLevel(targetLevelConfig.getLevel());
        permission.setWithdrawRate(targetLevelConfig.getWithdrawRate());
        permission.setExtraServiceCharge(targetLevelConfig.getExtraServiceCharge());
        permission.setUpdateUserId(operatorUserId);

        customerPermissionDao.updateLevelConfig(permission);
        customerRelationPoolSupport.updateLevelConfig(permission);
    }

    @Override
    public ApiResult<CustomerEnumsDto> queryPosCustomerEnums() {
        CustomerEnumsDto result = new CustomerEnumsDto();

        List<CommonEnumDto> auditStatusTypes = Stream.of(CustomerAuditStatus.values())
                .map(e -> new CommonEnumDto((byte) e.getCode(), e.getDesc()))
                .collect(Collectors.toList());
        result.setAuditStatusTypes(auditStatusTypes);

        Set<Integer> levels = customerLevelSupport.getLevels();
        final String lvStr = "Lv";
        List<CommonEnumDto> levelTypes = levels.stream()
                .map(e -> new CommonEnumDto(e.byteValue(), lvStr + e)).collect(Collectors.toList());
        result.setLevelTypes(levelTypes);

        return ApiResult.succ(result);
    }

    @Override
    public Pagination<CustomerIntegrateInfoDto> queryCustomerIntegrates(CustomerIntegrateCondition condition, OrderHelper orderHelper, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(CustomerIntegrateOrderField.getInstance());
        }

        int totalCount = customerIntegrateDao.getCustomerIntegrateCount(condition);

        Pagination<CustomerIntegrateInfoDto> result = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<CustomerIntegrateInfoDto> customers = customerIntegrateDao.queryCustomerIntegrates(
                    condition, orderHelper, limitHelper);
            result.setResult(customers);
        }
        return result;
    }

    @Override
    public CustomerIntegrateInfoDto findCustomerIntegrate(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return customerIntegrateDao.findCustomerIntegrate(userId);
    }

    @Override
    public ApiResult<NullObject> updateUserAvailable(Long userId, Boolean available, UserIdentifier operator) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(available, "available");
        FieldChecker.checkEmpty(operator, "operator");

        CustomerDto customer = customerService.findById(userId, true, true);
        if (customer == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }

        if (!available) {
            // 禁用用户，把在线的用户踢下线
            UserInfo kickedUser =  new UserInfo();
            kickedUser.setId(userId);
            kickedUser.setUserType(UserType.CUSTOMER.getValue());
            userSessionComponent.kickUser(kickedUser);
        }

        if (!available.equals(customer.isAvailable())) {
            CustomerDto afterCustomer = customer.copy();
            afterCustomer.setAvailable(available);
             customerService.update(customer, afterCustomer);
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> identifyPosUserInfo(IdentifyInfoDto identifyInfo) {
        FieldChecker.checkEmpty(identifyInfo, "identifyInfo");
        identifyInfo.check("identifyInfo");

        CustomerPermissionDto permission = getPermission(identifyInfo.getUserId());
        if (!identifyInfo.getUpdateKey().equals(permission.getUpdateTime())) {
            return ApiResult.fail(AuthorityErrorCode.AUDIT_STATUS_ERROR_IDENTITY_DATED);
        }
        if (CustomerAuditStatus.NOT_SUBMIT.equals(permission.parseAuditStatus())) {
            return ApiResult.fail(AuthorityErrorCode.AUDIT_STATUS_ERROR_NOU_SUBMIT_FOR_AUTHORIZE);
        }
        // FSM 状态机变更
        AuditStatusTransferContext transferContext = identifyInfo.buildStatusTransferContext();
        FSM fsm = AuthorityFSMFactory.newAuditInstance(permission.parseAuditStatus().toString(), transferContext);
        if (identifyInfo.isAllowed()) {
            fsm.processFSM("platAudited");
        } else {
            fsm.processFSM("platRejected");
        }

        return ApiResult.succ();
    }
}
