/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.impl;

import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.fsm.AuthorityFSMFactory;
import com.pos.authority.fsm.context.AuditStatusTransferContext;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.basic.service.SecurityService;
import com.pos.basic.sm.fsm.FSM;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.constants.BankCodeNameEnum;
import com.pos.transaction.constants.CardTypeEnum;
import com.pos.transaction.constants.CardUsageEnum;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.dao.PosCardDao;
import com.pos.transaction.domain.UserPosCard;
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.dto.request.BindCardDto;
import com.pos.transaction.exception.PosUserErrorCode;
import com.pos.transaction.exception.TransactionErrorCode;
import com.pos.transaction.helipay.action.QuickPayApi;
import com.pos.transaction.helipay.vo.SettlementCardBindResponseVo;
import com.pos.transaction.helipay.vo.SettlementCardBindVo;
import com.pos.transaction.service.PosCardService;
import com.pos.user.dao.UserDao;
import com.pos.user.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * POS 银行卡相关ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosCardServiceImpl implements PosCardService {

    @Resource
    private PosCardDao posCardDao;

    @Resource
    private UserDao userDao;

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private SecurityService securityService;

    @Resource
    private QuickPayApi quickPayApi;

    @Resource
    private PosConstants posConstants;

    @Override
    public BindCardDto getWithdrawCard(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        // 查询权限获取绑卡信息
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        BindCardDto withdrawCard = null;
        if (permission.getPosCardId() != null) {
            withdrawCard = new BindCardDto();
            withdrawCard.setName(permission.getIdCardName());

            // 查询绑定的收款银行卡信息
            PosCardDto posCard = posCardDao.getUserPosCard(permission.getPosCardId());
            if (posCard != null && userId.equals(posCard.getUserId())
                    && CardUsageEnum.IN_CARD.equals(posCard.parseCardUsage())) {
                withdrawCard.setPosCardId(permission.getPosCardId());
                withdrawCard.setCardNO(posCard.getCardNO());
                withdrawCard.setPhone(posCard.getMobilePhone());
                withdrawCard.setPosCardImage(permission.getPosCardImage());
                // 身份认证已通过和未审核的图片不允许修改，其它均可修改
                CustomerAuditStatus auditStatus = permission.parseAuditStatus();
                if (CustomerAuditStatus.AUDITED.equals(auditStatus)
                        || CustomerAuditStatus.NOT_AUDIT.equals(auditStatus)) {
                    withdrawCard.setPosCardImageCanModify(Boolean.FALSE);
                } else {
                    withdrawCard.setPosCardImageCanModify(Boolean.TRUE);
                }
            }
        }
        return withdrawCard;
    }

    @Override
    public ApiResult<PosCardDto> findWithdrawCard(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        // 查询权限获取绑卡信息
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        PosCardDto withdrawCard = null;
        if (permission.getPosCardId() == null) {
            // 查询绑定的收款银行卡信息
            withdrawCard = posCardDao.getUserPosCard(permission.getPosCardId());
        }

        return ApiResult.succ(withdrawCard);
    }

    @Override
    public ApiResult<NullObject> bindWithdrawCard(Long userId, BindCardDto withdrawCard) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(withdrawCard, "withdrawCard");
        withdrawCard.check("withdrawCard", securityService);

        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.AUDITED.equals(auditStatus)) {
            // 已通过状态，重复绑定
            return ApiResult.fail(TransactionErrorCode.BIND_CARD_ERROR_AUTHORITY_AUDIT_STATUS_AUDITED);
        } else if (CustomerAuditStatus.NOT_AUDIT.equals(auditStatus)) {
            // 未审核状态，重复绑定
            return ApiResult.fail(TransactionErrorCode.BIND_CARD_ERROR_AUTHORITY_AUDIT_STATUS_NOT_AUDIT);
        } else if (CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            // 未通过状态，更新绑定收款银行卡的图片信息，其它信息不做更新
            return updateBindCardImage(permission, withdrawCard);
        } else {
            // 未提交状态，新绑定收款银行卡
            return bindNewCard(permission, withdrawCard);
        }
    }

    private ApiResult<NullObject> updateBindCardImage(CustomerPermissionDto permission, BindCardDto bindCardInfo) {
        FieldChecker.checkEmpty(bindCardInfo.getPosCardImage(), "posCardImage");
        FieldChecker.checkEmpty(bindCardInfo.getPosCardId(), "posCardId");
        // 保存绑定的收款银行卡信息
        permission.setPosCardImage(bindCardInfo.getPosCardImage());
        permission.setUpdateUserId(permission.getUserId());
        customerAuthorityService.updateWithdrawCard(permission);
        // FSM 更新用户身份信息审核状态
        FSM fsm = AuthorityFSMFactory.newAuditInstance(permission.parseAuditStatus().toString(),
                new AuditStatusTransferContext(permission.getId(), permission.getUserId(), permission.getUserId()));
        fsm.processFSM("submitAll");
        return ApiResult.succ();
    }

    // TODO 待优化，与alterWithdrawCard(CustomerPermissionDto permission, BindCardDto withdrawCard)一起重构
    private ApiResult<NullObject> bindNewCard(CustomerPermissionDto permission, BindCardDto withdrawCard) {
        if (StringUtils.isEmpty(permission.getIdCardNo())) {
            return ApiResult.fail(TransactionErrorCode.BIND_CARD_ERROR_IDENTITY_NOT_EXISTED);
        }

        //解密之后的数据 调用接口时需要  数据库存储的是加密了的数据
        String decryptName = securityService.decryptData(permission.getIdCardName());
        String decryptCardNo = securityService.decryptData(withdrawCard.getCardNO());
        String decryptIdCardNo = securityService.decryptData(permission.getIdCardNo());
        String decryptPhone = securityService.decryptData(withdrawCard.getPhone());
        String orderId = UUID.randomUUID().toString();
        SettlementCardBindVo settlementCardBindVo = new SettlementCardBindVo();
        settlementCardBindVo.setP1_bizType("SettlementCardBind");
        settlementCardBindVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlementCardBindVo.setP3_userId(String.valueOf(permission.getUserId()));
        settlementCardBindVo.setP4_orderId(orderId);
        settlementCardBindVo.setP5_payerName(decryptName);
        settlementCardBindVo.setP6_idCardType("IDCARD");
        settlementCardBindVo.setP7_idCardNo(decryptIdCardNo);
        settlementCardBindVo.setP8_cardNo(decryptCardNo);
        settlementCardBindVo.setP9_phone(decryptPhone);
        ApiResult<SettlementCardBindResponseVo> apiResult = quickPayApi.settlementCardBind(settlementCardBindVo);
        if (apiResult.isSucc()) {
            SettlementCardBindResponseVo settlementCardBindResponseVo = apiResult.getData();
            // 保存银行卡信息
            UserPosCard userPosCard = new UserPosCard();
            userPosCard.setBank(BankCodeNameEnum.getEnum(settlementCardBindResponseVo.getRt8_bankId()).getDesc());
            userPosCard.setBankCode(settlementCardBindResponseVo.getRt8_bankId());
            userPosCard.setCardNO(withdrawCard.getCardNO());
            userPosCard.setCardType(CardTypeEnum.DEBIT_CARD.getCode());
            userPosCard.setCardUsage(CardUsageEnum.IN_CARD.getCode());
            userPosCard.setIdCardNO(permission.getIdCardNo());
            userPosCard.setMobilePhone(withdrawCard.getPhone());
            userPosCard.setName(permission.getIdCardName());
            userPosCard.setUserId(permission.getUserId());
            posCardDao.save(userPosCard);
            // 保存绑定的收款银行卡信息
            permission.setPosCardId(userPosCard.getId());
            permission.setPosCardImage(withdrawCard.getPosCardImage());
            permission.setUpdateUserId(permission.getUserId());
            customerAuthorityService.updateWithdrawCard(permission);
            // FSM 更新用户身份信息审核状态
            FSM fsm = AuthorityFSMFactory.newAuditInstance(permission.parseAuditStatus().toString(),
                    new AuditStatusTransferContext(permission.getId(), permission.getUserId(), permission.getUserId()));
            fsm.processFSM("submitAll");
            // 更新用户姓名
            User user = userDao.getById(permission.getUserId());
            if (StringUtils.isBlank(user.getName())) {
                user.setName(decryptName);
                userDao.update(user);
            }
            return ApiResult.succ();
        } else {
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    @Override
    public ApiResult<NullObject> alterWithdrawCard(Long userId, BindCardDto withdrawCard) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(withdrawCard, "withdrawCard");
        withdrawCard.check("withdrawCard", securityService);

        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (!CustomerAuditStatus.AUDITED.equals(auditStatus)) {
            ApiResult.fail(TransactionErrorCode.ALTER_BIND_CARD_ERROR_AUTHORITY_AUDIT_STATUS_AUDITED);
        }
        // TODO 交易类型改版实现完成后增加未完成交易判断

        return alterWithdrawCard(permission, withdrawCard);
    }

    // TODO 待优化，与bindNewCard一起重构
    private ApiResult<NullObject> alterWithdrawCard(CustomerPermissionDto permission, BindCardDto withdrawCard) {
        if (StringUtils.isEmpty(permission.getIdCardNo())) {
            return ApiResult.fail(TransactionErrorCode.BIND_CARD_ERROR_IDENTITY_NOT_EXISTED);
        }

        //解密之后的数据 调用接口时需要  数据库存储的是加密了的数据
        String decryptName = securityService.decryptData(permission.getIdCardName());
        String decryptCardNo = securityService.decryptData(withdrawCard.getCardNO());
        String decryptIdCardNo = securityService.decryptData(permission.getIdCardNo());
        String decryptPhone = securityService.decryptData(withdrawCard.getPhone());
        String orderId = UUID.randomUUID().toString();
        SettlementCardBindVo settlementCardBindVo = new SettlementCardBindVo();
        settlementCardBindVo.setP1_bizType("SettlementCardBind");
        settlementCardBindVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlementCardBindVo.setP3_userId(String.valueOf(permission.getUserId()));
        settlementCardBindVo.setP4_orderId(orderId);
        settlementCardBindVo.setP5_payerName(decryptName);
        settlementCardBindVo.setP6_idCardType("IDCARD");
        settlementCardBindVo.setP7_idCardNo(decryptIdCardNo);
        settlementCardBindVo.setP8_cardNo(decryptCardNo);
        settlementCardBindVo.setP9_phone(decryptPhone);
        settlementCardBindVo.setP11_operateType("UPDATE");
        ApiResult<SettlementCardBindResponseVo> apiResult = quickPayApi.settlementCardBind(settlementCardBindVo);
        if (apiResult.isSucc()) {
            SettlementCardBindResponseVo settlementCardBindResponseVo = apiResult.getData();
            // 保存银行卡信息
            UserPosCard userPosCard = new UserPosCard();
            userPosCard.setBank(BankCodeNameEnum.getEnum(settlementCardBindResponseVo.getRt8_bankId()).getDesc());
            userPosCard.setBankCode(settlementCardBindResponseVo.getRt8_bankId());
            userPosCard.setCardNO(withdrawCard.getCardNO());
            userPosCard.setCardType(CardTypeEnum.DEBIT_CARD.getCode());
            userPosCard.setCardUsage(CardUsageEnum.IN_CARD.getCode());
            userPosCard.setIdCardNO(permission.getIdCardNo());
            userPosCard.setMobilePhone(withdrawCard.getPhone());
            userPosCard.setName(permission.getIdCardName());
            userPosCard.setUserId(permission.getUserId());
            posCardDao.save(userPosCard);
            // 保存绑定的收款银行卡信息
            permission.setPosCardId(userPosCard.getId());
            permission.setPosCardImage(withdrawCard.getPosCardImage());
            permission.setUpdateUserId(permission.getUserId());
            customerAuthorityService.updateWithdrawCard(permission);
            // FSM 更新用户身份信息审核状态
            FSM fsm = AuthorityFSMFactory.newAuditInstance(permission.parseAuditStatus().toString(),
                    new AuditStatusTransferContext(permission.getId(), permission.getUserId(), permission.getUserId()));
            fsm.processFSM("alterSettlement");

            return ApiResult.succ();
        } else {
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    @Override
    public ApiResult<List<PosCardDto>> queryOutBankCard(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return ApiResult.succ(posCardDao.queryUserPosCard(userId, CardUsageEnum.OUT_CARD.getCode()));
    }

    @Override
    public ApiResult<NullObject> deleteOutBankCard(Long cardId, Long userId) {
        FieldChecker.checkEmpty(cardId, "cardId");
        FieldChecker.checkEmpty(userId, "userId");

        PosCardDto posCard = posCardDao.getUserPosCard(cardId);
        if (posCard == null
                || !userId.equals(posCard.getUserId())
                || !CardUsageEnum.OUT_CARD.equals(posCard.parseCardUsage())) {
            return ApiResult.fail(PosUserErrorCode.BANK_CARD_NOT_EXISTED);
        }
        posCardDao.delete(cardId);
        return ApiResult.succ();
    }
}
