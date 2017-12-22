/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.impl;

import com.google.common.collect.Lists;
import com.pos.authority.constant.CustomerAuditStatus;
import com.pos.authority.dto.level.CustomerUpgradeLevelDto;
import com.pos.authority.dto.permission.CustomerPermissionDto;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.exception.AuthorityErrorCode;
import com.pos.authority.fsm.AuthorityFSMFactory;
import com.pos.authority.fsm.context.AuditStatusTransferContext;
import com.pos.authority.service.CustomerAuthorityService;
import com.pos.authority.service.CustomerRelationService;
import com.pos.authority.service.CustomerStatisticsService;
import com.pos.basic.constant.RedisConstants;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.basic.sm.fsm.FSM;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.SHAUtils;
import com.pos.common.util.basic.UUIDUnsigned32;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.transaction.constants.*;
import com.pos.transaction.converter.PosConverter;
import com.pos.transaction.dao.*;
import com.pos.transaction.domain.*;
import com.pos.transaction.dto.*;
import com.pos.transaction.dto.auth.PosUserAuthDetailDto;
import com.pos.transaction.dto.auth.PosUserAuthDto;
import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.dto.card.PosCardValidInfoDto;
import com.pos.transaction.dto.get.QuickGetMoneyDto;
import com.pos.transaction.dto.identity.IdentifyInfoDto;
import com.pos.transaction.dto.request.GetMoneyDto;
import com.pos.transaction.dto.request.LevelUpgradeDto;
import com.pos.transaction.dto.transaction.SelectCardRequestDto;
import com.pos.transaction.dto.transaction.TransactionHandledInfoDto;
import com.pos.transaction.exception.PosUserErrorCode;
import com.pos.transaction.exception.TransactionErrorCode;
import com.pos.transaction.fsm.PosFSMFactory;
import com.pos.transaction.fsm.context.TransactionStatusTransferContext;
import com.pos.transaction.helipay.action.QuickPayApi;
import com.pos.transaction.helipay.vo.*;
import com.pos.transaction.service.PosCardService;
import com.pos.transaction.service.PosService;
import com.pos.user.dao.UserDao;
import com.pos.user.exception.UserErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.DigestException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 睿智
 * @version 1.0, 2017/8/22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosServiceImpl implements PosService {

    private static final Logger log = LoggerFactory.getLogger(PosServiceImpl.class);

    private static final long OUT_CARD_CACHE_SECOND_TIME = 86400L; // 交易付款银行卡信息缓存最大时间（1天）

    @Resource
    private PosDao posDao;

    @Resource
    private UserDao userDao;

    @Resource
    private GlobalConstants globalConstants;

    @Resource
    private QuickPayApi quickPayApi;

    @Resource
    private SecurityService securityService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private PosConstants posConstants;

    @Resource
    private PosCardDao posCardDao;

    @Resource
    private PosAuthDao posAuthDao;

    @Resource
    private RedisTemplate<String, PosCardDto> redisOutCardTemplate;

    @Resource
    private PosTwitterBrokerageDao posTwitterBrokerageDao;

    @Resource
    private PosUserTransactionHandledDao posUserTransactionHandledDao;

    @Resource
    private PosUserTransactionRecordDao posUserTransactionRecordDao;

    @Resource
    private PosUserJuniorDao posUserJuniorDao;

    @Resource
    private PosUserChannelDao posUserChannelDao;

    @Resource
    private TransactionFailureRecordDao transactionFailureRecordDao;

    @Resource
    private CustomerAuthorityService customerAuthorityService;

    @Resource
    private CustomerRelationService customerRelationService;

    @Resource
    private CustomerBrokerageDao customerBrokerageDao;

    @Resource
    private CustomerStatisticsService customerStatisticsService;

    @Resource
    private PosCardService posCardService;

    @Override
    public boolean updateAuditStatus(AuditStatusTransferContext transferContext, UserAuditStatus auditStatus) {
        // 参数校验
        FieldChecker.checkEmpty(transferContext, "transferContext");
        FieldChecker.checkEmpty(auditStatus, "auditStatus");
        FieldChecker.checkEmpty(transferContext.getUserId(), "userId");
        FieldChecker.checkEmpty(transferContext.getOperatorUserId(), "operatorUserId");
        if (UserAuditStatus.REJECTED.equals(auditStatus)) {
            FieldChecker.checkEmpty(transferContext.getRejectReason(), "rejectReason");
        }

        posAuthDao.updateAuditStatus(transferContext.getUserId(), auditStatus.getCode(),
                transferContext.getRejectReason(), transferContext.getOperatorUserId());
        return true;
    }

    @Override
    public ApiResult<PosUserAuditInfoDto> getAuditInfo(Long posAuthId, boolean decrypted) {
        FieldChecker.checkEmpty(posAuthId, "posAuthId");

        PosUserAuthDetailDto authDetail = posAuthDao.findAuthDetailById(posAuthId);
        if (authDetail == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (UserAuditStatus.NOT_SUBMIT.equals(authDetail.parseAuditStatus())) {
            return ApiResult.fail(PosUserErrorCode.NOT_SUBMIT_STATUS_ERROR);
        }
        PosUserAuditInfoDto auditInfo = new PosUserAuditInfoDto();
        auditInfo.setIdentityInfo(authDetail.buildPosUserIdentity(decrypted, securityService));
        auditInfo.setBindCardInfo(authDetail.buildBindCardInfo(decrypted, securityService));
        auditInfo.setUpdateKey(authDetail.getUpdateDate());

        return ApiResult.succ(auditInfo);
    }

    @Override
    public ApiResult<NullObject> identifyPosUserInfo(IdentifyInfoDto identifyInfo) {
        FieldChecker.checkEmpty(identifyInfo, "posAuthId");
        identifyInfo.check("identifyInfo");

        PosUserAuthDetailDto authDetail = posAuthDao.findAuthDetailById(identifyInfo.getPosAuthId());
        if (authDetail == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (!identifyInfo.getUpdateKey().equals(authDetail.getUpdateDate())) {
            return ApiResult.fail(PosUserErrorCode.IDENTITY_INFO_REFRESHED_ERROR);
        }
        if (UserAuditStatus.NOT_SUBMIT.equals(authDetail.parseAuditStatus())) {
            return ApiResult.fail(PosUserErrorCode.NOT_SUBMIT_STATUS_ERROR);
        }
        // FSM 状态机变更
        AuditStatusTransferContext transferContext = identifyInfo.buildStatusTransferContext();
        FSM fsm = AuthorityFSMFactory.newAuditInstance(authDetail.parseAuditStatus().toString(), transferContext);
        if (identifyInfo.isAllowed()) {
            fsm.processFSM("platAudited");
        } else {
            fsm.processFSM("platRejected");
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult<QuickGetMoneyDto> getQuickInfo(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.NOT_SUBMIT.equals(auditStatus)) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_AUTHORITY_AUDIT_STATUS_NOT_SUBMIT);
        } else if (CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_AUTHORITY_AUDIT_STATUS_REJECTED);
        }

        QuickGetMoneyDto result = new QuickGetMoneyDto();
        result.setPoundageRate(permission.getWithdrawRate());
        result.setPoundage(permission.getExtraServiceCharge());
        result.setArrival(posConstants.getPosArrival());
        result.hundredPercentPoundageRate(); // 百分化费率

        return ApiResult.succ(result);
    }

    @Override
    public ApiResult<CreateOrderDto> selectCreateRecord(Long userId, SelectCardRequestDto selectCardRequestDto, String ip) {
        // 参数校验
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(ip, "ip");
        FieldChecker.checkEmpty(selectCardRequestDto, "selectCardRequestDto");
        selectCardRequestDto.check("selectCardRequestDto", posConstants);

        // 身份权限验证
        ApiResult<CustomerPermissionDto> permissionCheckResult = checkCustomerPermission(userId);
        if (!permissionCheckResult.isSucc()) {
            return ApiResult.fail(permissionCheckResult.getError());
        }
        CustomerPermissionDto permission = permissionCheckResult.getData();

        // 付款银行卡校验
        PosCardDto outCard = posCardDao.getUserPosCard(selectCardRequestDto.getId());
        if (outCard == null || !outCard.getUserId().equals(userId)
                || !CardUsageEnum.OUT_CARD.equals(outCard.parseCardUsage())) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_OUT_BANK_CARD_NOT_EXISTED);
        }

        // 信用卡必填cvv2和有效期
        if (CardTypeEnum.CREDIT_CARD.equals(outCard.parseCardType())) {
            FieldChecker.checkEmpty(selectCardRequestDto.getCvv2(), "selectCardRequestDto.cvv2");
            FieldChecker.checkEmpty(selectCardRequestDto.getValidDate(), "selectCardRequestDto.validDate");
        }

        // 解密被加密的数据
        PosCardDto inCard = posCardDao.getUserPosCard(permission.getPosCardId());
        PosCardDto decryptedInCard = inCard.copy();
        posCardService.decryptPosCardInfo(decryptedInCard);
        PosCardDto decryptedOutCard = outCard.copy();
        posCardService.decryptPosCardInfo(decryptedOutCard);

        // 付款银行卡和收款银行卡所有人必须相同：姓名和身份证号相同
        if (!decryptedInCard.getName().equals(decryptedOutCard.getName())
                || !decryptedInCard.getIdCardNo().equals(decryptedOutCard.getIdCardNo())) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_CARD_USER_DIFFERENCE);
        }
        // 下单填入CVV2和有效期信息
        decryptedOutCard.setValidInfo(buildCardValidInfo(selectCardRequestDto.getCvv2(), selectCardRequestDto.getValidDate()));

        return createRecord(permission, decryptedOutCard, selectCardRequestDto.getAmount(), ip);
    }

    /**
     * 检查用户的快捷收款权限
     *
     * @param userId 用户userId
     * @return 校验通过：返回用户信息；校验失败：返回错误信息
     */
    private ApiResult<PosUserAuthDetailDto> checkUserGetAuth(Long userId) {
        PosUserAuthDetailDto authDetail = posAuthDao.findAuthDetail(userId);
        if (authDetail == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (UserAuditStatus.NOT_SUBMIT.equals(authDetail.parseAuditStatus())) {
            return ApiResult.fail(PosUserErrorCode.NOT_SUBMIT_ERROR_FOR_GET);
        }
        if (UserAuditStatus.REJECTED.equals(authDetail.parseAuditStatus())) {
            return ApiResult.fail(PosUserErrorCode.REJECTED_ERROR_FOR_GET);
        }
        if (!AuthStatusEnum.ENABLE.equals(authDetail.parseGetAuth())) {
            return ApiResult.fail(PosUserErrorCode.PERMISSION_GET_ERROR);
        }

        return ApiResult.succ(authDetail);
    }

    /**
     * 检查用户的权限信息
     *
     * @param userId 用户userId
     * @return 校验通过：返回用户权限信息；校验失败：返回错误信息
     */
    private ApiResult<CustomerPermissionDto> checkCustomerPermission(Long userId) {
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.NOT_SUBMIT.equals(auditStatus)) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_AUTHORITY_AUDIT_STATUS_NOT_SUBMIT);
        } else if (CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_AUTHORITY_AUDIT_STATUS_REJECTED);
        }

        return ApiResult.succ(permission);
    }

    private PosCardValidInfoDto buildCardValidInfo(String encryptedCvv2, String encryptedValidDate) {
        PosCardValidInfoDto validInfo = new PosCardValidInfoDto();

        validInfo.setCvv2(StringUtils.isEmpty(encryptedCvv2) ? "" : securityService.decryptData(encryptedCvv2));
        if (StringUtils.isEmpty(encryptedValidDate)) {
            validInfo.setValidYear("");
            validInfo.setValidMonth("");
        } else {
            String decryptedValidDate = securityService.decryptData(encryptedValidDate);
            validInfo.setValidYear(StringUtils.substring(decryptedValidDate, 2));
            validInfo.setValidMonth(StringUtils.substring(decryptedValidDate, 0, 2));
        }

        return validInfo;
    }

    /**
     * 解密GetMoneyDto信息<br/>
     * 此解密返回一个新的对象，需要调用者接收并保存，原对象不变
     *
     * @param source 需要被解密的GetMoneyDto信息
     * @return 解密后的GetMoneyDto信息
     */
    private GetMoneyDto decryptGetMoneyInfo(GetMoneyDto source) {
        GetMoneyDto target = source.copy();

        target.setName(securityService.decryptData(target.getName()));
        target.setIdCardNO(securityService.decryptData(target.getIdCardNO()));
        target.setCardNO(securityService.decryptData(target.getCardNO()));
        target.setMobilePhone(securityService.decryptData(target.getMobilePhone()));
        // CVV2和有效期为非必填数据
        if (StringUtils.isNotEmpty(target.getCvv2())) {
            target.setCvv2(securityService.decryptData(target.getCvv2()));
        }
        if (StringUtils.isNotEmpty(target.getValidDate())) {
            target.setValidDate(securityService.decryptData(target.getValidDate()));
        }

        return target;
    }

    @Override
    public ApiResult<CreateOrderDto> writeCreateRecord(GetMoneyDto getMoneyDto, Long userId, String ip) {
        // 参数校验
        FieldChecker.checkEmpty(getMoneyDto, "getMoneyDto");
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(ip, "ip");
        getMoneyDto.check("getMoneyDto", posConstants, securityService);

        // 身份权限验证
        ApiResult<CustomerPermissionDto> permissionCheckResult = checkCustomerPermission(userId);
        if (!permissionCheckResult.isSucc()) {
            return ApiResult.fail(permissionCheckResult.getError());
        }
        CustomerPermissionDto permission = permissionCheckResult.getData();

        // 银行卡校验
        PosCardDto inCard = posCardDao.getUserPosCard(permission.getPosCardId());
        // 解密被加密的数据
        PosCardDto decryptedInCard = inCard.copy();
        posCardService.decryptPosCardInfo(decryptedInCard);
        GetMoneyDto decryptedGetMoneyDto = decryptGetMoneyInfo(getMoneyDto);
        // 付款银行卡和收款银行卡所有人必须相同：姓名和身份证号相同
        if (!decryptedInCard.getName().equals(decryptedGetMoneyDto.getName())
                || !decryptedInCard.getIdCardNo().equals(decryptedGetMoneyDto.getIdCardNO())) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_CARD_USER_DIFFERENCE);
        }

        PosCardDto outCard = null;
        // 用户已保存的付款银行卡不再重复入库
        List<PosCardDto> outCards = posCardDao.queryUserPosCard(userId, CardUsageEnum.OUT_CARD.getCode());
        if (!CollectionUtils.isEmpty(outCards)) {
            for (PosCardDto card : outCards) {
                PosCardDto decryptedOutCard = card.copy();
                posCardService.decryptPosCardInfo(decryptedOutCard);
                if (decryptedOutCard.getCardNO().equals(decryptedGetMoneyDto.getCardNO())) {
                    outCard = card;
                    break;
                }
            }
        }
        // 用户录入了一张新的信用卡
        if (outCard == null) {
            outCard = PosConverter.toPosCardDto(getMoneyDto, userId);
            // 用户勾选保存银行卡信息，入库
            if (getMoneyDto.isRecordBankCard()) {
                UserPosCard newCard = PosConverter.toUserPosCard(outCard);
                posCardDao.save(newCard);
                outCard.setId(newCard.getId());
            }
        }
        // 下单填入CVV2和有效期信息
        outCard.setValidInfo(decryptedGetMoneyDto.buildValidInfo());

        PosCardDto decryptedOutCard = outCard.copy();
        posCardService.decryptPosCardInfo(decryptedOutCard);

        return createRecord(permission, decryptedOutCard, getMoneyDto.getAmount(), ip);
    }

    @Override
    public ApiResult<NullObject> sendPayValidateSmsCode(Long userId, Long recordId) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(recordId, "recordId");

        // 获取支付密码时交易校验：交易用户、交易类型、交易状态
        UserPosTransactionRecord transaction = posUserTransactionRecordDao.get(recordId);
        if (transaction == null
                || !transaction.getUserId().equals(userId)
                || !TransactionType.getEnum(transaction.getTransactionType()).canGetPaySmsCode()) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_TRANSACTION_NOT_EXISTED);
        }
        if (!TransactionStatusType.PREDICT_TRANSACTION.equals(
                TransactionStatusType.getEnum(transaction.getStatus()))) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_STATUS_FOR_SMS_CODE);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        PosCardDto outCard = redisOutCardTemplate.opsForValue().get(RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + recordId);
        SendValidateCodeVo sendValidateCodeVo = new SendValidateCodeVo();
        sendValidateCodeVo.setP1_bizType("QuickPaySendValidateCode");
        sendValidateCodeVo.setP2_customerNumber(globalConstants.helibaoMerchantNO);
        sendValidateCodeVo.setP3_orderId(transaction.getRecordNum());
        sendValidateCodeVo.setP4_timestamp(sdf.format(now));
        sendValidateCodeVo.setP5_phone(outCard.getMobilePhone());
        sendValidateCodeVo.setP6_smsSignature("【钱刷刷】");
        ApiResult<SendValidateCodeResponseVo> apiResult = quickPayApi.sendValidateCode(sendValidateCodeVo);
        if (!apiResult.isSucc()) {
            // 累计失败次数、保存失败信息
            recordTransactionFailureInfo(transaction, apiResult.getMessage());
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> confirmPay(Long userId, String smsCode, Long recordId, String ip) {
        // 参数校验
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(smsCode, "smsCode");
        FieldChecker.checkEmpty(recordId, "recordId");
        FieldChecker.checkEmpty(ip, "ip");
        // 确认支付时交易校验：交易用户、交易类型、交易状态
        UserPosTransactionRecord transaction = posUserTransactionRecordDao.get(recordId);
        if (transaction == null
                || !transaction.getUserId().equals(userId)
                || !TransactionType.NORMAL_WITHDRAW.equals(TransactionType.getEnum(transaction.getTransactionType()))) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_RECORD_NOT_EXISTED);
        }
        TransactionStatusType status = TransactionStatusType.getEnum(transaction.getStatus());
        if (!status.canConfirmPay()) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_PROGRESSED_ERROR);
        }

        ConfirmPayVo confirmPayVo = buildConfirmPayVo(transaction.getRecordNum(), smsCode, ip);
        ApiResult<ConfirmPayResponseVo> apiResult = quickPayApi.confirmPay(confirmPayVo);
        if (apiResult.isSucc() && "SUCCESS".equals(apiResult.getData().getRt9_orderStatus())) {
            // 支付到公司账户成功，交易状态 -> 交易处理中
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(recordId);
            context.setSerialNumber(apiResult.getData().getRt6_serialNumber());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(
                    TransactionStatusType.getEnum(transaction.getStatus()).toString(), context);
            fsm.processFSM("confirmPayTransaction");
            transaction.setStatus(TransactionStatusType.TRANSACTION_IN_PROGRESS.getCode());

            ApiResult<SettlementCardWithdrawResponseVo> payToUserResult = payToUser(transaction, apiResult.getData());
            if (!payToUserResult.isSucc()) {
                // 累计失败次数、保存失败信息
                recordTransactionFailureInfo(transaction, apiResult.getMessage());
                return ApiResult.fail(payToUserResult.getError(), payToUserResult.getMessage());
            }
            return ApiResult.succ();
        } else {
            // 累计失败次数、保存失败信息
            recordTransactionFailureInfo(transaction, apiResult.getMessage());
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    @Override
    public ApiResult<NullObject> confirmUpgradeLevel(Long userId, String smsCode, Long recordId, String ip) {
        // 参数校验
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(smsCode, "smsCode");
        FieldChecker.checkEmpty(recordId, "recordId");
        FieldChecker.checkEmpty(ip, "ip");

        // 确认支付时交易校验：交易用户、交易类型、交易状态
        UserPosTransactionRecord transaction = posUserTransactionRecordDao.get(recordId);
        if (transaction == null
                || !transaction.getUserId().equals(userId)
                || !TransactionType.LEVEL_UPGRADE.equals(TransactionType.getEnum(transaction.getTransactionType()))) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_RECORD_NOT_EXISTED);
        }
        TransactionStatusType status = TransactionStatusType.getEnum(transaction.getStatus());
        if (!status.canConfirmPay()) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_PROGRESSED_ERROR);
        }

        ConfirmPayVo confirmPayVo = buildConfirmPayVo(transaction.getRecordNum(), smsCode, ip);
        ApiResult<ConfirmPayResponseVo> apiResult = quickPayApi.confirmPay(confirmPayVo);
        if (apiResult.isSucc() && "SUCCESS".equals(apiResult.getData().getRt9_orderStatus())) {
            // 支付到公司账户成功，交易状态 -> 交易处理中
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(recordId);
            context.setSerialNumber(apiResult.getData().getRt6_serialNumber());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(
                    TransactionStatusType.getEnum(transaction.getStatus()).toString(), context);
            fsm.processFSM("confirmUpgradeLevel");

            PosCardDto outCard = redisOutCardTemplate.opsForValue().get(RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + transaction.getId());
            String bankName = BankCodeNameEnum.getEnum(apiResult.getData().getRt11_bankId()).getDesc();
            outCard.setBankName(bankName);
            outCard.setBankCode(apiResult.getData().getRt11_bankId());
            if ("DEBIT".equals(apiResult.getData().getRt12_onlineCardType())) {
                outCard.setCardType(CardTypeEnum.DEBIT_CARD.getCode());
            } else if ("CREDIT".equals(apiResult.getData().getRt12_onlineCardType())) {
                outCard.setCardType(CardTypeEnum.CREDIT_CARD.getCode());
            }
            if (outCard.getId() != null) {
                // 对已保存的付款银行卡进行数据更新
                outCard.setLastUseDate(new Date());
                UserPosCard card = PosConverter.toUserPosCard(outCard);
                posCardDao.update(card);
            }
            transaction.setOutCardInfo(JsonUtils.objectToJson(outCard.buildSimplePosOutCard()));
            posUserTransactionRecordDao.updateTransactionOutCardInfo(transaction);

            return ApiResult.succ();
        } else {
            // 累计失败次数、保存失败信息
            recordTransactionFailureInfo(transaction, apiResult.getMessage());
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    /**
     * 构建交易确认Vo
     *
     * @param orderId 为交易记录的recordNum
     * @param smsCode 短信验证码
     * @param ip      用户IP地址
     * @return 交易确认Vo
     */
    private ConfirmPayVo buildConfirmPayVo(String orderId, String smsCode, String ip) {
        ConfirmPayVo confirmPayVo = new ConfirmPayVo();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        confirmPayVo.setP1_bizType("QuickPayConfirmPay");
        confirmPayVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        confirmPayVo.setP3_orderId(orderId);
        confirmPayVo.setP4_timestamp(sdf.format(now));
        confirmPayVo.setP5_validateCode(smsCode);
        confirmPayVo.setP6_orderIp(ip);

        return confirmPayVo;
    }

    @Override
    public boolean updateTransactionStatus(TransactionStatusTransferContext context, TransactionStatusType targetStatus) {
        FieldChecker.checkEmpty(context, "context");
        FieldChecker.checkEmpty(targetStatus, "targetStatus");

        log.info("TransactionStatusTransferContext={}", JsonUtils.objectToJson(context));
        UserPosTransactionRecord transactionRecord = posUserTransactionRecordDao.get(context.getRecordId());
        transactionRecord.setStatus(targetStatus.getCode());
        Date currentTime = new Date();
        TransactionType transactionType = TransactionType.getEnum(transactionRecord.getTransactionType());
        switch (targetStatus) {
            case PREDICT_TRANSACTION:
                // 在合利宝下单成功
                break;
            case TRANSACTION_IN_PROGRESS:
                // 交易失败后重发交易处理时，不更换支付时间和支付流水号
                if (transactionRecord.getPayDate() == null) {
                    transactionRecord.setPayDate(currentTime);
                }
                if (StringUtils.isEmpty(transactionRecord.getHelibaoZhifuNum())) {
                    transactionRecord.setHelibaoZhifuNum(context.getSerialNumber());
                }
                break;
            case TRANSACTION_FAILED:
                transactionRecord.setCompleteDate(currentTime);
                transactionRecord.setHelibaoTixianNum(context.getSerialNumber());
                break;
            case TRANSACTION_SUCCESS:
                transactionRecord.setCompleteDate(currentTime);
                transactionRecord.setHelibaoTixianNum(context.getSerialNumber());
                if (TransactionType.LEVEL_UPGRADE.equals(transactionType)) {
                    customerStatisticsService.incrementUpgradeCharge(transactionRecord.getUserId(), transactionRecord.getAmount());
                } else if (TransactionType.NORMAL_WITHDRAW.equals(transactionType)) {
                    customerStatisticsService.incrementWithdrawAmount(transactionRecord.getUserId(), transactionRecord.getAmount());
                }
                break;
            case TRANSACTION_HANDLED_SUCCESS:
                // 手动处理没有流水号
                transactionRecord.setCompleteDate(currentTime);
                if (TransactionType.NORMAL_WITHDRAW.equals(transactionType)) {
                    customerStatisticsService.incrementWithdrawAmount(transactionRecord.getUserId(), transactionRecord.getAmount());
                }
                break;
            default:
                throw new IllegalStateException("非法的交易状态");
        }
        posUserTransactionRecordDao.updateTransaction(transactionRecord);

        return true;
    }

    @Override
    public boolean generateBrokerage(Long recordId) {
        FieldChecker.checkEmpty(recordId, "recordId");

        UserPosTransactionRecord transaction = posUserTransactionRecordDao.get(recordId);

        // 只有普通收款交易才生成交易佣金
        if (transaction != null &&
                TransactionType.NORMAL_WITHDRAW.equals(TransactionType.getEnum(transaction.getTransactionType()))) {
            Queue<CustomerRelationDto> participatorQueue = customerRelationService.generateBrokerageParticipatorQueue(transaction.getUserId());
            if (!CollectionUtils.isEmpty(participatorQueue) && participatorQueue.size() > 2) {
                // 有需要参与分佣的用户
                List<TransactionCustomerBrokerage> brokerages = Lists.newArrayList();
                CustomerRelationDto current = participatorQueue.poll();
                CustomerRelationDto nextParticipator = participatorQueue.poll();
                do {
                    // 生成佣金
                    TransactionCustomerBrokerage brokerage = new TransactionCustomerBrokerage();
                    brokerage.setTransactionId(recordId);
                    brokerage.setAncestorUserId(nextParticipator.getUserId());
                    brokerage.setLevel(nextParticipator.getLevel());
                    brokerage.setWithdrawRate(nextParticipator.getWithdrawRate());
                    BigDecimal brokerageRate = current.getWithdrawRate().subtract(nextParticipator.getWithdrawRate());
                    brokerageRate = brokerageRate.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : brokerageRate;
                    brokerage.setBrokerageRate(brokerageRate);
                    brokerage.setBrokerage(transaction.getAmount().multiply(brokerageRate).setScale(2, BigDecimal.ROUND_DOWN));

                    brokerages.add(brokerage);

                    current = nextParticipator.copy();
                    nextParticipator = participatorQueue.poll();
                } while (nextParticipator != null);

                customerBrokerageDao.saveBrokerages(brokerages);
                for (TransactionCustomerBrokerage brokerage : brokerages) {
                    customerStatisticsService.incrementBrokerage(brokerage.getAncestorUserId(), brokerage.getBrokerage());
                }
            }
        }
        return true;
    }

    /**
     * 生成交易佣金
     *
     * @param transactionRecord 交易记录信息
     * @param juniorInfo        推客客户关系信息
     * @param channelInfo       客户推客的推客推客关系信息
     * @return 交易佣金
     */
    private UserPosTwitterBrokerage buildTwitterBrokerage(UserPosTransactionRecord transactionRecord, UserPosJuniorInfo juniorInfo, UserPosChannelInfo channelInfo) {
        UserPosTwitterBrokerage brokerage = new UserPosTwitterBrokerage();

        brokerage.setRecordId(transactionRecord.getId());
        brokerage.setBaseRate(posConstants.getPosPoundageRate());

        // 1、计算直接推客应得的佣金信息
        brokerage.setAgentUserId(juniorInfo.getChannelUserId());
        PosUserAuthDto auth = posAuthDao.findAuth(juniorInfo.getChannelUserId());
        BigDecimal agentRate = brokerage.getBaseRate().subtract(auth.getGetRate());
        // 费率小于0则
        brokerage.setAgentRate(agentRate.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : agentRate);
        // 1.1、客户推客佣金 = 交易金额 * (基准费率 - 推客收款费率)
        BigDecimal agentCharge = transactionRecord.getAmount().multiply(brokerage.getAgentRate(), new MathContext(2));
        brokerage.setAgentCharge(agentCharge.setScale(2, BigDecimal.ROUND_DOWN));
        brokerage.setGetAgent(GetAgentEnum.NOT_GET.getCode());

        // 2、计算上级推客应得的佣金信息
        if (channelInfo != null
                && Boolean.TRUE.equals(channelInfo.getRelationAvailable())
                && channelInfo.getParentUserId() != 0) {
            brokerage.setParentAgentUserId(channelInfo.getParentUserId());
            brokerage.setParentAgentRate(posConstants.getPosParentTwitterBrokerageRate());
            // 2.1、父推客佣金 = 交易金额 * 父推客佣金费率
            BigDecimal parentAgentCharge = transactionRecord.getAmount().multiply(brokerage.getParentAgentRate());
            brokerage.setParentAgentCharge(parentAgentCharge.setScale(2, BigDecimal.ROUND_DOWN));
            brokerage.setGetParentAgent(GetAgentEnum.NOT_GET.getCode());
        }

        return brokerage;
    }

    public void payCallback(ConfirmPayResponseVo confirmPayResponseVo) {
        log.info("合力保支付回调信息：callBackInfo={}", confirmPayResponseVo);
        String recordNum = confirmPayResponseVo.getRt5_orderId();
        UserPosTransactionRecord userPosTransactionRecord = posDao.queryRecordByRecordNum(recordNum);
        if (userPosTransactionRecord != null && "SUCCESS".equals(confirmPayResponseVo.getRt9_orderStatus())) {
            // 抛弃异步打款给用户的操作，避免同步操作打款给用户时失败，异步又打款给用户成功的情形
            // 当同步操作打款用户失败后，由后台管理员操作手动处理
            log.info("交易recordId={}的金额amount={}已成功到公司账户。", userPosTransactionRecord.getId(), confirmPayResponseVo.getRt8_orderAmount());
        } else {
            log.error("交易记录recordNum={}不存在！", recordNum);
        }
    }

    /**
     * 提现到用户收款银行卡
     *
     * @param transaction    交易记录信息
     * @param confirmContext 支付确认信息
     * @return 提现结果
     */
    private ApiResult<SettlementCardWithdrawResponseVo> payToUser(
            UserPosTransactionRecord transaction,
            ConfirmPayResponseVo confirmContext) {
        Date currentTime = new Date();

        // 获取用户权限信息
        CustomerPermissionDto permission = customerAuthorityService.getPermission(transaction.getUserId());
        // 获取订单金额
        BigDecimal orderAmount = new BigDecimal(confirmContext.getRt8_orderAmount());
        if (!transaction.getAmount().equals(orderAmount)) {
            log.error("交易{}的金额{}与合力保返回金额{}存在差异！",
                    transaction.getId(),
                    transaction.getAmount(),
                    orderAmount);
        }
        log.info("-------------------------->订单的金额：" + orderAmount);
        // 计算平台收取的服务费：serviceCharge = orderAmount * userWithdrawRate + extraServiceCharge
        BigDecimal serviceCharge = orderAmount
                .multiply(permission.getWithdrawRate())
                .add(permission.getExtraServiceCharge())
                .setScale(2, BigDecimal.ROUND_UP);
        log.info("-------------------------->总的服务费：" + serviceCharge);
        // 合利宝收取的手续费
        BigDecimal payCharge = orderAmount
                .multiply(posConstants.getHelibaoPoundageRate())
                .setScale(2, BigDecimal.ROUND_UP);
        log.info("-------------------------->合利宝收取的手续费：" + payCharge);
        // 更新付款银行卡信息
        PosCardDto outCard = redisOutCardTemplate.opsForValue().get(RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + transaction.getId());
        String bankName = BankCodeNameEnum.getEnum(confirmContext.getRt11_bankId()).getDesc();
        outCard.setBankName(bankName);
        outCard.setBankCode(confirmContext.getRt11_bankId());
        if ("DEBIT".equals(confirmContext.getRt12_onlineCardType())) {
            outCard.setCardType(CardTypeEnum.DEBIT_CARD.getCode());
        } else if ("CREDIT".equals(confirmContext.getRt12_onlineCardType())) {
            outCard.setCardType(CardTypeEnum.CREDIT_CARD.getCode());
        }
        if (outCard.getId() != null) {
            // 对已保存的付款银行卡进行数据更新
            outCard.setLastUseDate(currentTime);
            UserPosCard card = PosConverter.toUserPosCard(outCard);
            posCardDao.update(card);
        }
        //提现的到账金额
        BigDecimal arrivalAmount = orderAmount.subtract(serviceCharge);
        log.info("-------------------------->提现的到账金额：" + arrivalAmount);
        //公司支付给用户时 合利宝的手续费
        BigDecimal posCharge = arrivalAmount
                .multiply(posConstants.getHelibaoTixianRate())
                .add(posConstants.getHelibaoTixianMoney())
                .setScale(2, BigDecimal.ROUND_UP);
        log.info("-------------------------->公司支付给用户时，合利宝的手续费：" + posCharge);
        // 更新交易相关信息
        transaction.setArrivalAmount(arrivalAmount);
        transaction.setPosCharge(posCharge);
        PosOutCardInfoDto simpleOut = outCard.buildSimplePosOutCard(); // 付款卡信息
        transaction.setOutCardInfo(JsonUtils.objectToJson(simpleOut));
        transaction.setAmount(orderAmount);
        transaction.setServiceCharge(serviceCharge);
        transaction.setPayCharge(payCharge);
        transaction.setHelibaoZhifuNum(confirmContext.getRt6_serialNumber());
        transaction.setPayDate(currentTime);
        posUserTransactionRecordDao.updateTransaction(transaction);
        // 发起提现
        SettlementCardWithdrawVo settlement = buildSettlementCardWithdrawVo(transaction);
        ApiResult<SettlementCardWithdrawResponseVo> withdrawResult = quickPayApi.settlementCardWithdraw(settlement);
        if (withdrawResult.isSucc()) {
            // 发起提现成功，加入交易轮询队列，由定时任务轮询处理交易状态
            redisTemplate.opsForList().rightPush(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE, transaction.getId().toString());
        } else {
            // 发起提现失败，更新交易状态-交易失败
            TransactionStatusType statusType = TransactionStatusType.getEnum(transaction.getStatus());
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(transaction.getId());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
            fsm.processFSM("withdrawFailed");
        }
        return withdrawResult;
    }

    public SettlementCardWithdrawVo buildSettlementCardWithdrawVo(UserPosTransactionRecord transactionRecord) {
        SettlementCardWithdrawVo settlement = new SettlementCardWithdrawVo();

        settlement.setP1_bizType("SettlementCardWithdraw");
        settlement.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlement.setP3_userId(String.valueOf(transactionRecord.getUserId()));
        settlement.setP4_orderId(transactionRecord.getRecordNum());
        settlement.setP5_amount(transactionRecord.getArrivalAmount().toString());
        settlement.setP6_feeType("PAYER");

        return settlement;
    }

    /**
     * 下单
     *
     * @param permission 下单用户权限信息
     * @param outCard    下单付款银行卡（PS：付款银行卡信息数据为解密后的数据）
     * @param amount     下单金额
     * @param ip         下单用户IP地址
     * @return 下单结果
     */
    private ApiResult<CreateOrderDto> createRecord(CustomerPermissionDto permission, PosCardDto outCard, BigDecimal amount, String ip) {
        UserPosTransactionRecord originTransaction = buildAndSaveOriginNormalTransaction(permission, amount, outCard);

        CreateOrderVo createOrderVo = buildCreateOrderVo(originTransaction, outCard, ip);
        ApiResult apiResult;
        try {
            apiResult = quickPayApi.createOrder(createOrderVo);
        } catch (Exception e) {
            apiResult = ApiResult.fail(new ErrorCode() {
                @Override
                public int getCode() {
                    return -1;
                }

                @Override
                public String getMessage() {
                    return "网络异常";
                }
            });
        }

        if (apiResult.isSucc()) {
            // 状态机变更
            TransactionStatusType statusType = TransactionStatusType.getEnum(originTransaction.getStatus());
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(originTransaction.getId());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
            fsm.processFSM("transactionCreateSuccess");
            // 缓存交易的付款银行卡信息
            redisOutCardTemplate.opsForValue().set(
                    RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + originTransaction.getId(),
                    outCard, OUT_CARD_CACHE_SECOND_TIME, TimeUnit.SECONDS);
            // 构建返回结果集
            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.setAmount(originTransaction.getAmount());
            createOrderDto.setCardNO(outCard.getCardNO());
            createOrderDto.setId(originTransaction.getId());
            return ApiResult.succ(createOrderDto);
        } else {
            // 累计失败次数、保存失败信息
            recordTransactionFailureInfo(originTransaction, apiResult.getMessage());

            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    /**
     * 保存交易失败时的相关信息
     *
     * @param transaction   交易信息
     * @param failureReason 失败原因
     */
    private void recordTransactionFailureInfo(UserPosTransactionRecord transaction, String failureReason) {
        // 累计失败次数
        posUserTransactionRecordDao.incrementFailureTimes(transaction.getId());
        // 保存失败信息
        TransactionFailureRecord failureRecord = new TransactionFailureRecord();
        failureRecord.setTransactionId(transaction.getId());
        failureRecord.setFailureReason(failureReason);
        transactionFailureRecordDao.save(failureRecord);
    }

    /**
     * 构建和保存一个客户收款交易
     *
     * @param permission 客户权限信息
     * @param amount     交易金额
     * @param outCard    付款银行卡
     * @return 交易原始信息
     */
    private UserPosTransactionRecord buildAndSaveOriginNormalTransaction(
            CustomerPermissionDto permission, BigDecimal amount, PosCardDto outCard) {
        UserPosTransactionRecord transaction = new UserPosTransactionRecord();

        transaction.setRecordNum(UUIDUnsigned32.randomUUIDString());
        transaction.setTransactionType(TransactionType.NORMAL_WITHDRAW.getCode());
        transaction.setInCardId(permission.getPosCardId());
        transaction.setOutCardInfo(JsonUtils.objectToJson(outCard.buildSimplePosOutCard()));
        transaction.setUserId(permission.getUserId());
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatusType.ORIGIN_TRANSACTION.getCode());
        posUserTransactionRecordDao.saveNormalTransaction(transaction);

        return transaction;
    }

    private CreateOrderVo buildCreateOrderVo(UserPosTransactionRecord originTransaction, PosCardDto outCard, String ip) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setP1_bizType("QuickPayBankCardPay");
        createOrderVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        createOrderVo.setP3_userId(String.valueOf(originTransaction.getUserId()));
        createOrderVo.setP4_orderId(originTransaction.getRecordNum());
        createOrderVo.setP5_timestamp(sdf.format(now));
        createOrderVo.setP6_payerName(outCard.getName());
        createOrderVo.setP7_idCardType("IDCARD");
        createOrderVo.setP8_idCardNo(outCard.getIdCardNo());
        createOrderVo.setP9_cardNo(outCard.getCardNO());
        createOrderVo.setP10_year(outCard.getValidInfo().getValidYear());
        createOrderVo.setP11_month(outCard.getValidInfo().getValidMonth());
        createOrderVo.setP12_cvv2(outCard.getValidInfo().getCvv2());
        createOrderVo.setP13_phone(outCard.getMobilePhone());
        createOrderVo.setP14_currency("CNY");
        createOrderVo.setP15_orderAmount(originTransaction.getAmount().toPlainString());
        createOrderVo.setP16_goodsName("家装费");
        createOrderVo.setP18_terminalType("OTHER");
        String terminalId = UUID.randomUUID().toString();
        createOrderVo.setP19_terminalId(terminalId);
        createOrderVo.setP20_orderIp(ip);
        createOrderVo.setP23_serverCallbackUrl(posConstants.getHelibaoCallbackUrl());

        return createOrderVo;
    }

    public Map<String, String> getAllBankLogo() {
        Map<String, String> logos = new HashMap<>();
        List<BankLogoDto> bankLogoDtos = posDao.queryBankLogo();
        bankLogoDtos.forEach(e -> logos.put(e.getBankCode(), e.getBankLogo()));
        return logos;
    }


    public ApiResult<GetSignDto> getSign(String url) {

        String ticket = redisTemplate.opsForValue().get(RedisConstants.WECHAT_JSAPI_TICKET);//在redis里面缓存的ticket

        if (StringUtils.isBlank(ticket)) {

            String accessToken = redisTemplate.opsForValue().get(RedisConstants.WECHAT_ACCESS_TOKEN);//在redis里面缓存的access_token

            if (StringUtils.isBlank(accessToken)) {
                String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + posConstants.getPosWeChatAppId() + "&secret=" + posConstants.getPosWeChatSecret();

                String accessTokenResultStr = HttpRequestUtils.httpRequest(accessTokenUrl, "GET", null);
                if (StringUtils.isNotBlank(accessTokenResultStr)) {
                    WechatAccessTokenDto wechatAccessTokenDto = JsonUtils.jsonToObject(accessTokenResultStr, new TypeReference<WechatAccessTokenDto>() {
                    });
                    if (!wechatAccessTokenDto.accessTokenRequestSucc()) {
                        log.error("获取微信access_token失败。errcode={},errmsg={}", wechatAccessTokenDto.getErrcode(), wechatAccessTokenDto.getErrmsg());
                        return ApiResult.fail(PosUserErrorCode.WECHAT_SIGN_ERROR_FOR_ACCESS_TOKEN);
                    }
                    redisTemplate.opsForValue().set(RedisConstants.WECHAT_ACCESS_TOKEN, wechatAccessTokenDto.getAccess_token(), wechatAccessTokenDto.getExpires_in(), TimeUnit.SECONDS);
                    String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
                    String ticketResultStr = HttpRequestUtils.httpRequest(ticketUrl, "GET", null);

                    if (StringUtils.isNotBlank(ticketResultStr)) {
                        WechatTicketDto wechatTicketDto = JsonUtils.jsonToObject(ticketResultStr, new TypeReference<WechatTicketDto>() {
                        });
                        redisTemplate.opsForValue().set(RedisConstants.WECHAT_JSAPI_TICKET, wechatTicketDto.getTicket(), wechatTicketDto.getExpires_in(), TimeUnit.SECONDS);
                        ticket = wechatTicketDto.getTicket();
                    }
                }
            } else {

                String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
                String ticketResultStr = HttpRequestUtils.httpRequest(ticketUrl, "GET", null);

                if (StringUtils.isNotBlank(ticketResultStr)) {
                    WechatTicketDto wechatTicketDto = JsonUtils.jsonToObject(ticketResultStr, new TypeReference<WechatTicketDto>() {
                    });
                    redisTemplate.opsForValue().set(RedisConstants.WECHAT_JSAPI_TICKET, wechatTicketDto.getTicket(), wechatTicketDto.getExpires_in(), TimeUnit.SECONDS);
                    ticket = wechatTicketDto.getTicket();
                }
            }
        }
        String rndStr = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis() / 1000L;
        String sign = "";
        Map<String, Object> params = new HashMap<>();
        params.put("noncestr", rndStr);
        params.put("jsapi_ticket", ticket);
        params.put("url", url);
        params.put("timestamp", timestamp);
        try {
            sign = SHAUtils.SHA1(params);
        } catch (DigestException e) {
            e.printStackTrace();
        }
        GetSignDto getSignDto = new GetSignDto();
        getSignDto.setAppId(posConstants.getPosWeChatAppId());
        getSignDto.setNonceStr(rndStr);
        getSignDto.setSignature(sign);
        getSignDto.setTimestamp(String.valueOf(timestamp));
        return ApiResult.succ(getSignDto);
    }

    @Override
    public ApiResult<NullObject> handledTransaction(Long recordId, TransactionHandledInfoDto handledInfo, UserIdentifier operator) {
        FieldChecker.checkEmpty(recordId, "recordId");
        FieldChecker.checkEmpty(handledInfo, "handledInfo");
        FieldChecker.checkEmpty(operator, "operator");
        handledInfo.check("handledInfo");

        UserPosTransactionRecord record = posUserTransactionRecordDao.get(recordId);
        if (record == null) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_TRANSACTION_NOT_EXISTED);
        }
        TransactionStatusType statusType = TransactionStatusType.getEnum(record.getStatus());
        if (!TransactionStatusType.TRANSACTION_FAILED.equals(statusType)) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_TRANSACTION_STATUS_ERROR);
        }
        UserPosTransactionHandledInfo saveInfo = new UserPosTransactionHandledInfo();
        BeanUtils.copyProperties(handledInfo, saveInfo);
        saveInfo.setCreateUserId(operator.getUserId());
        saveInfo.setCreateDate(new Date());
        posUserTransactionHandledDao.save(saveInfo);

        TransactionStatusTransferContext context = new TransactionStatusTransferContext();
        context.setRecordId(recordId);
        FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
        fsm.processFSM("platHandledSuccess");

        return ApiResult.succ();
    }

    @Override
    public ApiResult<TransactionHandledInfoDto> getHandledTransactionInfo(Long recordId) {
        FieldChecker.checkEmpty(recordId, "recordId");

        return ApiResult.succ(posUserTransactionHandledDao.getByRecordId(recordId));
    }

    @Override
    public ApiResult<NullObject> againPayToPosUserForFailed(Long recordId, UserIdentifier operator) {
        FieldChecker.checkEmpty(recordId, "recordId");
        FieldChecker.checkEmpty(operator, "operator");

        // 交易校验
        UserPosTransactionRecord record = posUserTransactionRecordDao.get(recordId);
        if (record == null) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_TRANSACTION_NOT_EXISTED);
        }
        TransactionStatusType statusType = TransactionStatusType.getEnum(record.getStatus());
        if (!TransactionStatusType.TRANSACTION_FAILED.equals(statusType)) {
            return ApiResult.fail(TransactionErrorCode.POS_ERROR_TRANSACTION_STATUS_ERROR);
        }
        // 重发提现请求
        SettlementCardWithdrawVo settlement = buildSettlementCardWithdrawVo(record);
        ApiResult<SettlementCardWithdrawResponseVo> withdrawResult = quickPayApi.settlementCardWithdraw(settlement);
        if (withdrawResult.isSucc()) {
            // 状态变更
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(recordId);
            FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
            fsm.processFSM("platRepeatWithdraw");
            // 发起提现成功，加入交易轮询队列，由定时任务轮询处理交易状态
            redisTemplate.opsForList().rightPush(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE, recordId.toString());
        } else {
            // 发起提现失败，更新交易状态-交易失败，累计失败次数、保存失败信息
            recordTransactionFailureInfo(record, withdrawResult.getMessage());
            return ApiResult.fail(withdrawResult.getError(), withdrawResult.getMessage());
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult<BigDecimal> withdrawBrokerage(CustomerPermissionDto permission, BigDecimal brokerage) {
        FieldChecker.checkEmpty(permission, "permission");
        FieldChecker.checkMinValue(brokerage, BigDecimal.ZERO, "brokerage");

        UserPosTransactionRecord transaction = buildAndSaveOriginBrokerageTransaction(permission, brokerage);

        return payBrokerageToUser(transaction);
    }

    /**
     * 支付佣金到用户收款银行卡
     *
     * @param transaction 佣金交易
     * @return 佣金金额
     */
    private ApiResult<BigDecimal> payBrokerageToUser(UserPosTransactionRecord transaction) {
        SettlementCardWithdrawVo settlement = new SettlementCardWithdrawVo();

        settlement.setP1_bizType("SettlementCardWithdraw");
        settlement.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlement.setP3_userId(String.valueOf(transaction.getUserId()));
        settlement.setP4_orderId(transaction.getRecordNum());
        settlement.setP5_amount(transaction.getArrivalAmount().toString());
        settlement.setP6_feeType("RECEIVER"); // 平台支付手续费

        ApiResult<SettlementCardWithdrawResponseVo> withdrawResult = quickPayApi.settlementCardWithdraw(settlement);
        if (withdrawResult.isSucc()) {
            // 发起提现成功，加入交易轮询队列，由定时任务轮询处理交易状态
            redisTemplate.opsForList().rightPush(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE, transaction.getId().toString());
            return ApiResult.succ(transaction.getArrivalAmount());
        } else {
            // 发起提现失败，更新交易状态-交易失败
            log.error("佣金提现交易{}失败，原因：{}", transaction.getId(), withdrawResult.getMessage());
            TransactionStatusType statusType = TransactionStatusType.getEnum(transaction.getStatus());
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(transaction.getId());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
            fsm.processFSM("withdrawFailed");

            recordTransactionFailureInfo(transaction, withdrawResult.getMessage());
            return ApiResult.fail(withdrawResult.getError(), withdrawResult.getMessage());
        }
    }

    /**
     * 构建和保存一个佣金提现交易
     *
     * @param permission 客户权限信息
     * @param brokerage  佣金金额
     * @return 交易原始信息
     */
    private UserPosTransactionRecord buildAndSaveOriginBrokerageTransaction(
            CustomerPermissionDto permission, BigDecimal brokerage) {
        UserPosTransactionRecord transaction = new UserPosTransactionRecord();

        transaction.setRecordNum(UUIDUnsigned32.randomUUIDString());
        transaction.setTransactionType(TransactionType.BROKERAGE_WITHDRAW.getCode());
        transaction.setInCardId(permission.getPosCardId());
        transaction.setUserId(permission.getUserId());
        transaction.setAmount(brokerage);
        transaction.setArrivalAmount(brokerage);
        BigDecimal posCharge = brokerage
                .multiply(posConstants.getHelibaoTixianRate())
                .add(posConstants.getHelibaoTixianMoney())
                .setScale(2, BigDecimal.ROUND_UP);
        transaction.setPosCharge(posCharge);
        transaction.setStatus(TransactionStatusType.TRANSACTION_IN_PROGRESS.getCode());

        posUserTransactionRecordDao.saveBrokerageTransaction(transaction);

        return transaction;
    }

    @Override
    public ApiResult<CreateOrderDto> createLevelUpgradeTransaction(Long userId, LevelUpgradeDto levelUpgradeInfo, String ip) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(levelUpgradeInfo, "levelUpgradeInfo");
        levelUpgradeInfo.check("levelUpgradeInfo", securityService);

        // 权限校验
        CustomerPermissionDto permission = customerAuthorityService.getPermission(userId);
        if (permission == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        CustomerAuditStatus auditStatus = permission.parseAuditStatus();
        if (CustomerAuditStatus.NOT_SUBMIT.equals(auditStatus)
                || CustomerAuditStatus.REJECTED.equals(auditStatus)) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_AUDIT_STATUS_FOR_PAY);
        }
        // 等级校验
        ApiResult<CustomerUpgradeLevelDto> upgradeCheckResult =
                customerAuthorityService.getCustomerUpgradeInfo(userId, levelUpgradeInfo.getTargetLevel());
        if (!upgradeCheckResult.isSucc()) {
            return ApiResult.fail(upgradeCheckResult.getError());
        }
        // 服务费金额校验
        if (upgradeCheckResult.getData().getLevelPriceDifference().compareTo(levelUpgradeInfo.getAmount()) > 0) {
            return ApiResult.fail(AuthorityErrorCode.UPGRADE_ERROR_CHARGE_NOT_ENOUGH);
        }

        String decryptedBankCardNo = securityService.decryptData(levelUpgradeInfo.getBankCardNo());

        PosCardDto outCard = null;
        // 用户已保存的付款银行卡不再重复入库
        List<PosCardDto> outCards = posCardDao.queryUserPosCard(userId, CardUsageEnum.OUT_CARD.getCode());
        if (!CollectionUtils.isEmpty(outCards)) {
            for (PosCardDto card : outCards) {
                PosCardDto decryptedOutCard = card.copy();
                posCardService.decryptPosCardInfo(decryptedOutCard);
                if (decryptedOutCard.getCardNO().equals(decryptedBankCardNo)) {
                    outCard = card;
                    break;
                }
            }
        }
        // 用户录入了一张新的信用卡
        if (outCard == null) {
            outCard = PosConverter.toPosCardDto(permission, levelUpgradeInfo);
            // 用户勾选保存银行卡信息，入库
            if (levelUpgradeInfo.isRecordBankCard()) {
                UserPosCard newCard = PosConverter.toUserPosCard(outCard);
                posCardDao.save(newCard);
                outCard.setId(newCard.getId());
            }
        }
        // 下单填入CVV2和有效期信息
        outCard.setValidInfo(buildCardValidInfo(levelUpgradeInfo.getCvv2(), levelUpgradeInfo.getValidDate()));

        posCardService.decryptPosCardInfo(outCard);
        return createUpgradeTransaction(permission, outCard, levelUpgradeInfo.getAmount(), ip);
    }

    private ApiResult<CreateOrderDto> createUpgradeTransaction(CustomerPermissionDto permission, PosCardDto outCard, BigDecimal amount, String ip) {
        UserPosTransactionRecord transaction = buildAndSaveOriginUpgradeTransaction(permission, amount, outCard);

        CreateOrderVo createOrderVo = buildCreateOrderVo(transaction, outCard, ip);
        ApiResult apiResult = quickPayApi.createOrder(createOrderVo);
        if (apiResult.isSucc()) {
            // 状态机变更
            TransactionStatusType statusType = TransactionStatusType.getEnum(transaction.getStatus());
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(transaction.getId());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
            fsm.processFSM("transactionCreateSuccess");
            // 缓存交易的付款银行卡信息
            redisOutCardTemplate.opsForValue().set(
                    RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + transaction.getId(),
                    outCard, OUT_CARD_CACHE_SECOND_TIME, TimeUnit.SECONDS);
            // 构建返回结果集
            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.setAmount(transaction.getAmount());
            createOrderDto.setCardNO(outCard.getCardNO());
            createOrderDto.setId(transaction.getId());
            return ApiResult.succ(createOrderDto);
        } else {
            // 累计失败次数、保存失败信息
            recordTransactionFailureInfo(transaction, apiResult.getMessage());
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    /**
     * 构建和保存一个等级晋升交易
     *
     * @param permission           客户权限信息
     * @param upgradeServiceCharge 等级晋升服务费
     * @param outCard              付款银行卡信息
     * @return 交易原始信息
     */
    private UserPosTransactionRecord buildAndSaveOriginUpgradeTransaction(
            CustomerPermissionDto permission, BigDecimal upgradeServiceCharge, PosCardDto outCard) {
        UserPosTransactionRecord transaction = new UserPosTransactionRecord();

        transaction.setRecordNum(UUIDUnsigned32.randomUUIDString());
        transaction.setTransactionType(TransactionType.LEVEL_UPGRADE.getCode());
        transaction.setInCardId(0L);
        transaction.setOutCardInfo(JsonUtils.objectToJson(outCard.buildSimplePosOutCard()));
        transaction.setUserId(permission.getUserId());
        transaction.setAmount(upgradeServiceCharge);
        BigDecimal payCharge = upgradeServiceCharge
                .multiply(posConstants.getHelibaoPoundageRate())
                .setScale(2, BigDecimal.ROUND_UP);
        transaction.setPayCharge(payCharge);
        transaction.setStatus(TransactionStatusType.ORIGIN_TRANSACTION.getCode());

        posUserTransactionRecordDao.saveUpgradeTransaction(transaction);

        return transaction;
    }
}
