/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.basic.constant.RedisConstants;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.service.SecurityService;
import com.pos.basic.sm.fsm.FSM;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.SHAUtils;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.pos.constants.*;
import com.pos.pos.converter.PosConverter;
import com.pos.pos.dao.*;
import com.pos.pos.domain.*;
import com.pos.pos.dto.*;
import com.pos.pos.dto.auth.AuthorityDetailDto;
import com.pos.pos.dto.auth.AuthorityDto;
import com.pos.pos.dto.card.PosCardDto;
import com.pos.pos.dto.card.PosCardValidInfoDto;
import com.pos.pos.dto.get.QuickGetMoneyDto;
import com.pos.pos.dto.identity.IdentifyInfoDto;
import com.pos.pos.dto.request.BindCardDto;
import com.pos.pos.dto.request.GetMoneyDto;
import com.pos.pos.dto.transaction.SelectCardRequestDto;
import com.pos.pos.dto.transaction.TransactionHandledInfoDto;
import com.pos.pos.dto.user.PosUserIdentityDto;
import com.pos.pos.exception.PosUserErrorCode;
import com.pos.pos.fsm.PosFSMFactory;
import com.pos.pos.fsm.context.AuditStatusTransferContext;
import com.pos.pos.fsm.context.TransactionStatusTransferContext;
import com.pos.pos.helipay.action.QuickPayApi;
import com.pos.pos.helipay.util.PosErrorCode;
import com.pos.pos.helipay.vo.*;
import com.pos.pos.service.PosService;
import com.pos.user.exception.UserErrorCode;
import org.apache.commons.lang3.StringUtils;
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
 * 主要业务Service实现类
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosServiceImpl implements PosService {

    private static final Logger log = LoggerFactory.getLogger(PosServiceImpl.class);

    @Resource
    private PosDao posDao;

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
    private AuthorityDao authorityDao;

    @Resource
    private RedisTemplate<String, PosCardDto> redisOutCardTemplate;

    @Resource
    private TwitterBrokerageDao twitterBrokerageDao;

    @Resource
    private PosUserTransactionHandledDao posUserTransactionHandledDao;

    @Resource
    private PosTwitterDao posTwitterDao;

    @Override
    public AuthorityDetailDto findAuthDetail(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return authorityDao.findAuthDetail(userId);
    }

    @Override
    public AuthorityDto findAuth(Long userId) {
        FieldChecker.checkEmpty(userId, "userId");

        return authorityDao.findAuthByUserId(userId);
    }

    @Override
    public PosUserIdentityDto getIdentityInfo(Long userId, boolean decrypted) {
        FieldChecker.checkEmpty(userId, "userId");

        PosUserIdentityDto identity = null;
        AuthorityDto authInfo = authorityDao.findAuthByUserId(userId);
        if (authInfo != null && StringUtils.isNotEmpty(authInfo.getIdCardNo())) {
            // 身份认证-1相关信息中默认其中一个不为空，则其他也不为空
            identity = authInfo.buildPosUserIdentity(decrypted, securityService);
        }
        return identity;
    }

    @Override
    public ApiResult<NullObject> updateIdentityInfo(Long userId, PosUserIdentityDto identityInfo) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(identityInfo, "identityInfo");
        identityInfo.check("identityInfo", securityService);

        AuthorityDto authInfo = authorityDao.findAuthByUserId(userId);
        if (authInfo == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        UserAuditStatus auditStatus = authInfo.parseAuditStatus();
        if (UserAuditStatus.AUDITED.equals(auditStatus)) {
            return ApiResult.fail(PosUserErrorCode.AUDIT_STATUS_ERROR);
        } else if (UserAuditStatus.NOT_AUDIT.equals(auditStatus)) {
            return ApiResult.fail(PosUserErrorCode.NOT_AUDIT_STATUS_ERROR);
        } else if (UserAuditStatus.NOT_SUBMIT.equals(auditStatus)) {
            // 未提交状态设置姓名和身份证号
            authInfo.setIdCardName(identityInfo.getRealName());
            authInfo.setIdCardNo(identityInfo.getIdCardNo());
        }
        authInfo.setIdImageA(identityInfo.getIdImageA());
        authInfo.setIdImageB(identityInfo.getIdImageB());
        authInfo.setIdHoldImage(identityInfo.getIdHoldImage());
        authorityDao.updateIdentityInfo(authInfo);

        return ApiResult.succ();
    }

    @Override
    public BindCardDto getBindCardInfo(Long userId, boolean decrypted) {
        FieldChecker.checkEmpty(userId, "userId");

        AuthorityDetailDto authDetail = authorityDao.findAuthDetail(userId);
        BindCardDto bindCardInfo = null;
        if (authDetail != null && authDetail.getPosCardId() != null) {
            // posCardId不为空，则默认用户绑定收款银行卡成功
            bindCardInfo = authDetail.buildBindCardInfo(decrypted, securityService);
        }
        return bindCardInfo;
    }

    private ApiResult<NullObject> bindNewCard(AuthorityDetailDto authDetail, BindCardDto bindCardInfo) {
        if (StringUtils.isEmpty(authDetail.getIdCardNo())) {
            return ApiResult.fail(PosUserErrorCode.IDENTITY_INFO_ERROR);
        }
        FieldChecker.checkEmpty(bindCardInfo.getCardNO(), "cardNo");
        FieldChecker.checkEmpty(bindCardInfo.getName(), "name");
        FieldChecker.checkEmpty(bindCardInfo.getPhone(), "phone");
        FieldChecker.checkEmpty(bindCardInfo.getPosCardImage(), "posCardImage");

        //解密之后的数据 调用接口时需要  数据库存储的是加密了的数据
        String decryptName = securityService.decryptData(bindCardInfo.getName());
        String decryptCardNo = securityService.decryptData(bindCardInfo.getCardNO());
        String decryptIdCardNo = securityService.decryptData(authDetail.getIdCardNo());
        String decryptPhone = securityService.decryptData(bindCardInfo.getPhone());
        String orderId = UUID.randomUUID().toString();
        SettlementCardBindVo settlementCardBindVo = new SettlementCardBindVo();
        settlementCardBindVo.setP1_bizType("SettlementCardBind");
        settlementCardBindVo.setP2_customerNumber(globalConstants.helibaoMerchantNO);
        settlementCardBindVo.setP3_userId(String.valueOf(authDetail.getUserId()));
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
            PosBankCard posBankCard = new PosBankCard();
            posBankCard.setBankName(BankCodeNameEnum.getEnum(settlementCardBindResponseVo.getRt8_bankId()).getDesc());
            posBankCard.setBankCode(settlementCardBindResponseVo.getRt8_bankId());
            posBankCard.setBankCardNo(bindCardInfo.getCardNO());
            posBankCard.setCardType(CardTypeEnum.DEBIT_CARD.getCode());
            posBankCard.setCardUsage(CardUsageEnum.IN_CARD.getCode());
            posBankCard.setIdCardNo(authDetail.getIdCardNo());
            posBankCard.setMobilePhone(bindCardInfo.getPhone());
            posBankCard.setHolderName(bindCardInfo.getName());
            posBankCard.setUserId(authDetail.getUserId());
            posCardDao.save(posBankCard);
            // 保存绑定的收款银行卡信息
            authDetail.setPosCardId(posBankCard.getId());
            authDetail.setPosCardImage(bindCardInfo.getPosCardImage());
            authDetail.setUpdateUserId(authDetail.getUserId());
            authorityDao.updatePosCardInfo(authDetail);
            // FSM 更新用户身份信息审核状态
            FSM fsm = PosFSMFactory.newPosAuditInstance(authDetail.parseAuditStatus().toString(),
                    new AuditStatusTransferContext(authDetail.getId(), authDetail.getUserId()));
            fsm.processFSM("submitAll");

            return ApiResult.succ();
        } else {
            return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
        }
    }

    /**
     * 身份认证信息审核未通过，重新提交审核只更换绑卡图片
     *
     * @param authDetail   用户详细信息
     * @param bindCardInfo 绑卡信息
     * @return 操作结果
     */
    private ApiResult<NullObject> updateBindCardImage(AuthorityDetailDto authDetail, BindCardDto bindCardInfo) {
        FieldChecker.checkEmpty(bindCardInfo.getPosCardImage(), "posCardImage");
        FieldChecker.checkEmpty(bindCardInfo.getPosCardId(), "posCardId");
        // 保存绑定的收款银行卡信息
        authDetail.setPosCardImage(bindCardInfo.getPosCardImage());
        authDetail.setUpdateUserId(authDetail.getUserId());
        authorityDao.updatePosCardInfo(authDetail);
        // FSM 更新用户身份信息审核状态
        FSM fsm = PosFSMFactory.newPosAuditInstance(authDetail.parseAuditStatus().toString(),
                new AuditStatusTransferContext(authDetail.getId(), authDetail.getUserId()));
        fsm.processFSM("submitAll");
        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> bindCard(BindCardDto bindCardInfo, Long userId) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(bindCardInfo, "bindCardInfo");

        AuthorityDetailDto authDetail = authorityDao.findAuthDetail(userId);
        if (authDetail == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        UserAuditStatus auditStatus = authDetail.parseAuditStatus();
        if (UserAuditStatus.AUDITED.equals(auditStatus)) {
            // 已通过状态，重复绑定
            return ApiResult.fail(PosUserErrorCode.AUDIT_STATUS_ERROR);
        } else if (UserAuditStatus.NOT_AUDIT.equals(auditStatus)) {
            // 未审核状态，重复绑定
            return ApiResult.fail(PosUserErrorCode.NOT_AUDIT_STATUS_ERROR);
        } else if (UserAuditStatus.REJECTED.equals(auditStatus)) {
            // 未通过状态，更新绑定收款银行卡的图片信息，其它信息不做更新
            return updateBindCardImage(authDetail, bindCardInfo);
        } else {
            // 未提交状态，新绑定收款银行卡
            return bindNewCard(authDetail, bindCardInfo);
        }
    }

    @Override
    public boolean updateAuditStatus(AuditStatusTransferContext transferContext, UserAuditStatus auditStatus) {
        // 参数校验
        FieldChecker.checkEmpty(transferContext, "transferContext");
        FieldChecker.checkEmpty(auditStatus, "auditStatus");
        FieldChecker.checkEmpty(transferContext.getPosAuthId(), "posAuthId");
        FieldChecker.checkEmpty(transferContext.getOperatorUserId(), "operatorUserId");
        if (UserAuditStatus.REJECTED.equals(auditStatus)) {
            FieldChecker.checkEmpty(transferContext.getRejectReason(), "rejectReason");
        }

        authorityDao.updateAuditStatus(transferContext.getPosAuthId(), auditStatus.getCode(),
                transferContext.getRejectReason(), transferContext.getOperatorUserId());
        return true;
    }

    @Override
    public ApiResult<PosUserAuditInfoDto> getAuditInfo(Long posAuthId, boolean decrypted) {
        FieldChecker.checkEmpty(posAuthId, "posAuthId");

        AuthorityDetailDto authDetail = authorityDao.findAuthDetailById(posAuthId);
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

        AuthorityDetailDto authDetail = authorityDao.findAuthDetailById(identifyInfo.getPosAuthId());
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
        FSM fsm = PosFSMFactory.newPosAuditInstance(authDetail.parseAuditStatus().toString(), transferContext);
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

        AuthorityDto auth = authorityDao.findAuthByUserId(userId);
        if (auth == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_EXISTED);
        }
        if (UserAuditStatus.NOT_SUBMIT.equals(auth.parseAuditStatus())) {
            return ApiResult.fail(PosUserErrorCode.NOT_SUBMIT_ERROR_FOR_GET);
        }
        if (UserAuditStatus.REJECTED.equals(auth.parseAuditStatus())) {
            return ApiResult.fail(PosUserErrorCode.REJECTED_ERROR_FOR_GET);
        }
        if (!AuthStatusEnum.ENABLE.equals(auth.parseGetAuth())) {
            return ApiResult.fail(PosUserErrorCode.PERMISSION_GET_ERROR);
        }
        QuickGetMoneyDto result = new QuickGetMoneyDto();
        result.setPoundageRate(auth.getGetRate());
        result.setPoundage(posConstants.getPosExtraPoundage());
        result.setArrival(posConstants.getPosArrival());
        result.setCardBaseInfos(posCardDao.queryUserPosCard(userId, CardUsageEnum.OUT_CARD.getCode()));
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
        ApiResult<AuthorityDetailDto> authCheckResult = checkUserGetAuth(userId);
        if (!authCheckResult.isSucc()) {
            return ApiResult.fail(authCheckResult.getError());
        }
        AuthorityDetailDto authDetail = authCheckResult.getData();

        // 付款银行卡校验
        PosCardDto outCard = posCardDao.getUserPosCard(selectCardRequestDto.getId());
        if (outCard == null || !outCard.getUserId().equals(userId)) {
            return ApiResult.fail(PosUserErrorCode.BANK_CARD_NOT_EXISTED);
        }
        // 信用卡必填cvv2和有效期
        if (CardTypeEnum.CREDIT_CARD.equals(outCard.parseCardType())) {
            FieldChecker.checkEmpty(selectCardRequestDto.getCvv2(), "selectCardRequestDto.cvv2");
            FieldChecker.checkEmpty(selectCardRequestDto.getValidDate(), "selectCardRequestDto.validDate");
        }

        // 解密被加密的数据
        PosCardDto inCard = authDetail.buildUserInCard();
        PosCardDto decryptedInCard = decryptPosCardInfo(inCard);
        PosCardDto decryptedOutCard = decryptPosCardInfo(outCard);

        // 付款银行卡和收款银行卡所有人必须相同：姓名和身份证号相同
        if (!decryptedInCard.getHolderName().equals(decryptedOutCard.getHolderName())
                || !decryptedInCard.getIdCardNo().equals(decryptedOutCard.getIdCardNo())) {
            return ApiResult.fail(PosErrorCode.CARD_MSG_IS_WRONG);
        }
        // 下单填入CVV2和有效期信息
        decryptedOutCard.setValidInfo(buildCardValidInfo(selectCardRequestDto.getCvv2(), selectCardRequestDto.getValidDate()));

        return createRecord(authDetail, decryptedOutCard, selectCardRequestDto.getAmount(), ip);
    }

    /**
     * 检查用户的快捷收款权限
     *
     * @param userId 用户userId
     * @return 校验通过：返回用户信息；校验失败：返回错误信息
     */
    private ApiResult<AuthorityDetailDto> checkUserGetAuth(Long userId) {
        AuthorityDetailDto authDetail = authorityDao.findAuthDetail(userId);
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
     * 解密银行卡信息<br/>
     * 此解密返回一个新的对象，需要调用者接收并保存，原对象不变
     *
     * @param source 需要被解密的银行卡
     * @return 解密后的银行卡信息
     */
    private PosCardDto decryptPosCardInfo(PosCardDto source) {
        PosCardDto target = source.copy();

        target.setHolderName(securityService.decryptData(target.getHolderName()));
        target.setIdCardNo(securityService.decryptData(target.getIdCardNo()));
        target.setBankCardNo(securityService.decryptData(target.getBankCardNo()));
        target.setMobilePhone(securityService.decryptData(target.getMobilePhone()));

        return target;
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
        ApiResult<AuthorityDetailDto> authCheckResult = checkUserGetAuth(userId);
        if (!authCheckResult.isSucc()) {
            return ApiResult.fail(authCheckResult.getError());
        }
        AuthorityDetailDto authDetail = authCheckResult.getData();

        // 银行卡校验
        PosCardDto inCard = authDetail.buildUserInCard();
        if (inCard == null) {
            return ApiResult.fail(PosErrorCode.IN_CARD_NOT_EXIST);
        }
        // 解密被加密的数据
        PosCardDto decryptedInCard = decryptPosCardInfo(inCard);
        GetMoneyDto decryptedGetMoneyDto = decryptGetMoneyInfo(getMoneyDto);
        // 付款银行卡和收款银行卡所有人必须相同：姓名和身份证号相同
        if (!decryptedInCard.getHolderName().equals(decryptedGetMoneyDto.getName())
                || !decryptedInCard.getIdCardNo().equals(decryptedGetMoneyDto.getIdCardNO())) {
            return ApiResult.fail(PosErrorCode.CARD_MSG_IS_WRONG);
        }

        PosCardDto outCard = null;
        // 用户已保存的付款银行卡不再重复入库
        List<PosCardDto> outCards = posCardDao.queryUserPosCard(userId, CardUsageEnum.OUT_CARD.getCode());
        if (!CollectionUtils.isEmpty(outCards)) {
            for (PosCardDto card : outCards) {
                PosCardDto decryptedOutCard = decryptPosCardInfo(card);
                if (decryptedOutCard.getBankCardNo().equals(decryptedGetMoneyDto.getCardNO())) {
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
                PosBankCard newCard = PosConverter.toUserPosCard(outCard);
                posCardDao.save(newCard);
                outCard.setId(newCard.getId());
            }
        }
        // 下单填入CVV2和有效期信息
        outCard.setValidInfo(decryptedGetMoneyDto.buildValidInfo());

        return createRecord(authDetail, decryptPosCardInfo(outCard), getMoneyDto.getAmount(), ip);
    }

    @Override
    public ApiResult<NullObject> sendPayValidateSmsCode(Long userId, Long recordId) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(recordId, "recordId");

        PosTransaction transactionRecord = posDao.queryRecordById(recordId);
        if (transactionRecord == null
                || !transactionRecord.getUserId().equals(userId)) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_RECORD_NOT_EXISTED);
        }
        if (!TransactionStatusType.PREDICT_TRANSACTION.equals(
                TransactionStatusType.getEnum(transactionRecord.getStatus()))) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_DUPLICATE_SMS_CODE);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        PosCardDto outCard = redisOutCardTemplate.opsForValue().get(RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + recordId);
        SendValidateCodeVo sendValidateCodeVo = new SendValidateCodeVo();
        sendValidateCodeVo.setP1_bizType("QuickPaySendValidateCode");
        sendValidateCodeVo.setP2_customerNumber(globalConstants.helibaoMerchantNO);
        sendValidateCodeVo.setP3_orderId(transactionRecord.getRecordNum());
        sendValidateCodeVo.setP4_timestamp(sdf.format(now));
        sendValidateCodeVo.setP5_phone(outCard.getMobilePhone());
        sendValidateCodeVo.setP6_smsSignature("【钱刷刷】");
        ApiResult<SendValidateCodeResponseVo> apiResult = quickPayApi.sendValidateCode(sendValidateCodeVo);
        if (!apiResult.isSucc()) {
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
        // 交易校验
        PosTransaction transactionRecord = posDao.queryRecordById(recordId);
        if (transactionRecord == null || !transactionRecord.getUserId().equals(userId)) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_RECORD_NOT_EXISTED);
        }
        TransactionStatusType status = TransactionStatusType.getEnum(transactionRecord.getStatus());
        if (!status.canConfirmPay()) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_PROGRESSED_ERROR);
        }

        ConfirmPayVo confirmPayVo = buildConfirmPayVo(transactionRecord.getRecordNum(), smsCode, ip);
        ApiResult<ConfirmPayResponseVo> apiResult = quickPayApi.confirmPay(confirmPayVo);
        if (apiResult.isSucc() && "SUCCESS".equals(apiResult.getData().getRt9_orderStatus())) {
            // 支付到公司账户成功，交易状态 -> 交易处理中
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(recordId);
            context.setSerialNumber(apiResult.getData().getRt6_serialNumber());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(
                    TransactionStatusType.getEnum(transactionRecord.getStatus()).toString(), context);
            fsm.processFSM("confirmPayTransaction");
            transactionRecord.setStatus(TransactionStatusType.TRANSACTION_IN_PROGRESS.getCode());

            ApiResult<SettlementCardWithdrawResponseVo> payToUserResult = payToUser(transactionRecord, apiResult.getData());
            if (!payToUserResult.isSucc()) {
                return ApiResult.fail(payToUserResult.getError(), payToUserResult.getMessage());
            }
            return ApiResult.succ();
        }
        return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
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
        PosTransaction transactionRecord = posDao.queryRecordById(context.getRecordId());
        transactionRecord.setStatus(targetStatus.getCode());
        Date currentTime = new Date();
        switch (targetStatus) {
            case TRANSACTION_IN_PROGRESS:
                // 交易失败后重发交易处理时，不更换支付时间和支付流水号
                if (transactionRecord.getPayTime() == null) {
                    transactionRecord.setPayTime(currentTime);
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
                break;
            case TRANSACTION_HANDLED_SUCCESS:
                // 手动处理没有流水号
                transactionRecord.setCompleteDate(currentTime);
                break;
            default:
                throw new IllegalStateException("非法的交易状态");
        }
        posDao.updatePosRecord(transactionRecord);

        return true;
    }

    @Override
    public boolean generateBrokerage(Long recordId) {
        FieldChecker.checkEmpty(recordId, "recordId");

        PosTransaction transactionRecord = posDao.queryRecordById(recordId);
        // 查询用户的推客客户关系
        TwitterCustomer twitterCustomer = posTwitterDao.getTwitterCustomerByUserId(transactionRecord.getUserId());
        if (twitterCustomer == null || Boolean.FALSE.equals(twitterCustomer.getAvailable())) {
            // 自然客户或已解除推客客户关系，不生成相关佣金，直接返回成功
            return true;
        }
        // 查询用户推客的上级推客信息
        Twitter child = posTwitterDao.getTwitterById(twitterCustomer.getTwitterId());
        Twitter parent = posTwitterDao.getParentTwitterByChild(twitterCustomer.getTwitterId());
        TwitterBrokerage brokerage = buildTwitterBrokerage(transactionRecord, child, parent);

        twitterBrokerageDao.save(brokerage);
        return true;
    }

    /**
     * 生成交易佣金
     *
     * @param transaction 交易记录信息
     * @param child       子推客
     * @param parent      父推客
     * @return 交易佣金
     */
    private TwitterBrokerage buildTwitterBrokerage(PosTransaction transaction, Twitter child, Twitter parent) {
        Date now = new Date();
        TwitterBrokerage brokerage = new TwitterBrokerage();

        brokerage.setTransactionId(transaction.getId());
        brokerage.setBaseRate(posConstants.getPosPoundageRate());

        // 1、计算直接推客应得的佣金信息
        brokerage.setTwitterUserId(child.getUserId());
        AuthorityDto auth = authorityDao.findAuthByUserId(child.getUserId());
        // 推客佣金费率 = 基准费率 - 推客收款费率
        BigDecimal agentRate = brokerage.getBaseRate().subtract(auth.getGetRate());
        // 费率小于0则
        brokerage.setTwitterBrokerageRate(agentRate.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : agentRate);
        // 1.1、客户推客佣金 = 交易金额 * 推客佣金费率
        BigDecimal agentCharge = transaction.getAmount().multiply(brokerage.getTwitterBrokerageRate(), new MathContext(2));
        brokerage.setTwitterBrokerage(agentCharge.setScale(2, BigDecimal.ROUND_DOWN));
        brokerage.setTwitterBrokerageStatus(GetAgentEnum.NOT_GET.getCode());
        brokerage.setTwitterBrokerageStatusTime(now);

        // 2、计算上级推客应得的佣金信息
        if (parent != null) {
            brokerage.setParentUserId(parent.getUserId());
            brokerage.setParentBrokerageRate(posConstants.getPosParentTwitterBrokerageRate());
            // 2.1、父推客佣金 = 交易金额 * 父推客佣金费率
            BigDecimal parentAgentCharge = transaction.getAmount().multiply(brokerage.getParentBrokerageRate());
            brokerage.setParentBrokerage(parentAgentCharge.setScale(2, BigDecimal.ROUND_DOWN));
            brokerage.setParentBrokerageStatus(GetAgentEnum.NOT_GET.getCode());
            brokerage.setParentBrokerageStatusTime(now);
        }

        return brokerage;
    }

    public void payCallback(ConfirmPayResponseVo confirmPayResponseVo) {
        log.info("合力保支付回调信息：callBackInfo={}", confirmPayResponseVo);
        String recordNum = confirmPayResponseVo.getRt5_orderId();
        PosTransaction posTransaction = posDao.queryRecordByRecordNum(recordNum);
        if (posTransaction != null && "SUCCESS".equals(confirmPayResponseVo.getRt9_orderStatus())) {
            // 抛弃异步打款给用户的操作，避免同步操作打款给用户时失败，异步又打款给用户成功的情形
            // 当同步操作打款用户失败后，由后台管理员操作手动处理
            log.info("交易recordId={}的金额amount={}已成功到公司账户。", posTransaction.getId(), confirmPayResponseVo.getRt8_orderAmount());
        } else {
            log.error("交易记录recordNum={}不存在！", recordNum);
        }
    }

    /**
     * 提现到用户收款银行卡
     *
     * @param transactionRecord 交易记录信息
     * @param confirmContext    支付确认信息
     * @return 提现结果
     */
    private ApiResult<SettlementCardWithdrawResponseVo> payToUser(
            PosTransaction transactionRecord,
            ConfirmPayResponseVo confirmContext) {
        Date currentTime = new Date();
        // 获取用户信息
        AuthorityDto authInfo = authorityDao.findAuthByUserId(transactionRecord.getUserId());
        // 获取订单金额
        BigDecimal orderAmount = new BigDecimal(confirmContext.getRt8_orderAmount());
        if (!transactionRecord.getAmount().equals(orderAmount)) {
            log.error("交易{}的金额{}与合力保返回金额{}存在差异！",
                    transactionRecord.getId(),
                    transactionRecord.getAmount(),
                    orderAmount);
        }
        log.info("-------------------------->订单的金额：" + orderAmount);
        // 计算平台收取的服务费：serviceCharge = orderAmount * userGetRate + extra
        BigDecimal serviceCharge = orderAmount
                .multiply(authInfo.getGetRate())
                .add(posConstants.getPosExtraPoundage())
                .setScale(2, BigDecimal.ROUND_UP);
        log.info("-------------------------->总的服务费：" + serviceCharge);
        // 合利宝收取的手续费
        BigDecimal payCharge = orderAmount
                .multiply(posConstants.getHelibaoPoundageRate())
                .setScale(2, BigDecimal.ROUND_UP);
        log.info("-------------------------->合利宝收取的手续费：" + payCharge);
        // 更新付款银行卡信息
        PosCardDto outCard = redisOutCardTemplate.opsForValue().get(RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + transactionRecord.getId());
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
            outCard.setLastUseTime(currentTime);
            PosBankCard card = PosConverter.toUserPosCard(outCard);
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
        transactionRecord.setArrivalAmount(arrivalAmount);
        transactionRecord.setPosCharge(posCharge);
        PosOutCardInfoDto simpleOut = outCard.buildSimplePosOutCard(); // 付款卡信息
        transactionRecord.setOutCardInfo(JsonUtils.objectToJson(simpleOut));
        transactionRecord.setAmount(orderAmount);
        transactionRecord.setServiceCharge(serviceCharge);
        transactionRecord.setPayCharge(payCharge);
        transactionRecord.setHelibaoZhifuNum(confirmContext.getRt6_serialNumber());
        transactionRecord.setPayTime(currentTime);
        posDao.updatePosRecord(transactionRecord);
        // 发起提现
        SettlementCardWithdrawVo settlement = buildSettlementCardWithdrawVo(transactionRecord, arrivalAmount);
        ApiResult<SettlementCardWithdrawResponseVo> withdrawResult = quickPayApi.settlementCardWithdraw(settlement);
        if (withdrawResult.isSucc()) {
            // 发起提现成功，加入交易轮询队列，由定时任务轮询处理交易状态
            redisTemplate.opsForList().rightPush(RedisConstants.POS_TRANSACTION_WITHDRAW_QUEUE, transactionRecord.getId().toString());
        } else {
            // 发起提现失败，更新交易状态-交易失败
            TransactionStatusType statusType = TransactionStatusType.getEnum(transactionRecord.getStatus());
            TransactionStatusTransferContext context = new TransactionStatusTransferContext();
            context.setRecordId(transactionRecord.getId());
            FSM fsm = PosFSMFactory.newPosTransactionInstance(statusType.toString(), context);
            fsm.processFSM("withdrawFailed");
        }
        return withdrawResult;
    }

    private SettlementCardWithdrawVo buildSettlementCardWithdrawVo(
            PosTransaction transactionRecord, BigDecimal arrivalAmount) {
        SettlementCardWithdrawVo settlement = new SettlementCardWithdrawVo();

        settlement.setP1_bizType("SettlementCardWithdraw");
        settlement.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        settlement.setP3_userId(String.valueOf(transactionRecord.getUserId()));
        settlement.setP4_orderId(transactionRecord.getRecordNum());
        settlement.setP5_amount(arrivalAmount.toString());
        settlement.setP6_feeType("PAYER");

        return settlement;
    }

    /**
     * 下单
     *
     * @param authDetail 下单用户信息
     * @param outCard    下单付款银行卡（PS：付款银行卡信息数据为解密后的数据）
     * @param amount     下单金额
     * @param ip         下单用户IP地址
     * @return 下单结果
     */
    private ApiResult<CreateOrderDto> createRecord(AuthorityDetailDto authDetail, PosCardDto outCard, BigDecimal amount, String ip) {
        CostAndCompanyDto costAndCompanyDto = getCostType(authDetail.getUserId());
        CreateOrderVo createOrderVo = buildCreateOrderVo(authDetail.getUserId(), outCard, amount, ip, costAndCompanyDto);
        ApiResult apiResult = quickPayApi.createOrder(createOrderVo);
        if (apiResult.isSucc()) {
            PosTransaction posTransaction = saveTransactionRecord(
                    authDetail, costAndCompanyDto, createOrderVo, amount);
            // 缓存交易的付款银行卡信息
            redisOutCardTemplate.opsForValue().set(RedisConstants.POS_TRANSACTION_OUT_CARD_INFO + posTransaction.getId(), outCard);

            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.setAmount(posTransaction.getAmount());
            createOrderDto.setCardNO(outCard.getBankCardNo());
            createOrderDto.setId(posTransaction.getId());
            return ApiResult.succ(createOrderDto);
        }
        return ApiResult.fail(apiResult.getError(), apiResult.getMessage());
    }

    private PosTransaction saveTransactionRecord(
            AuthorityDetailDto authDetail, CostAndCompanyDto costAndCompanyDto,
            CreateOrderVo createOrderVo, BigDecimal amount) {
        PosTransaction posTransaction = new PosTransaction();
        posTransaction.setCompanyId(costAndCompanyDto.getCompanyId());
        posTransaction.setInCardId(authDetail.getPosCardId());
        posTransaction.setRecordNum(createOrderVo.getP4_orderId());
        posTransaction.setCostType(costAndCompanyDto.getCostTypeEnum().getCode());
        posTransaction.setUserId(authDetail.getUserId());
        posTransaction.setAmount(amount);
        posTransaction.setStatus(TransactionStatusType.PREDICT_TRANSACTION.getCode());
        posDao.addUserPosRecord(posTransaction);
        return posTransaction;
    }

    private CreateOrderVo buildCreateOrderVo(Long userId, PosCardDto outCard, BigDecimal amount, String ip, CostAndCompanyDto costAndCompanyDto) {
        String orderId = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setP1_bizType("QuickPayBankCardPay");
        createOrderVo.setP2_customerNumber(posConstants.getHelibaoMerchantNO());
        createOrderVo.setP3_userId(String.valueOf(userId));
        createOrderVo.setP4_orderId(orderId);
        createOrderVo.setP5_timestamp(sdf.format(now));
        createOrderVo.setP6_payerName(outCard.getHolderName());
        createOrderVo.setP7_idCardType("IDCARD");
        createOrderVo.setP8_idCardNo(outCard.getIdCardNo());
        createOrderVo.setP9_cardNo(outCard.getBankCardNo());
        createOrderVo.setP10_year(outCard.getValidInfo().getValidYear());
        createOrderVo.setP11_month(outCard.getValidInfo().getValidMonth());
        createOrderVo.setP12_cvv2(outCard.getValidInfo().getCvv2());
        createOrderVo.setP13_phone(outCard.getMobilePhone());
        createOrderVo.setP14_currency("CNY");
        createOrderVo.setP15_orderAmount(amount.toPlainString());
        createOrderVo.setP16_goodsName(costAndCompanyDto.getCostTypeEnum().getDesc());
        createOrderVo.setP18_terminalType("OTHER");
        String terminalId = UUID.randomUUID().toString();
        createOrderVo.setP19_terminalId(terminalId);
        createOrderVo.setP20_orderIp(ip);
        createOrderVo.setP23_serverCallbackUrl(posConstants.getHelibaoCallbackUrl());

        return createOrderVo;
    }

    private CostAndCompanyDto getCostType(long userId) {
        CostAndCompanyDto costAndCompanyDto = new CostAndCompanyDto();
        Date now = new Date();
        List<PosTransaction> records = posDao.queryRecordByUserIdAndCostType(userId, 0);
        if (!CollectionUtils.isEmpty(records)) {
            PosTransaction first = records.get(0);
            if (SimpleDateUtils.daysOfDuration(first.getCreateTime(), now) > 180) {
                costAndCompanyDto.setCostTypeEnum(CostTypeEnum.DESIGN);
                costAndCompanyDto.setCompanyId(getCompanyId());
            } else {
                records = posDao.queryRecordByUserIdAndCostType(userId, CostTypeEnum.CONSTRUCTION.getCode());
                if (!CollectionUtils.isEmpty(records)) {
                    first = records.get(0);
                    if (SimpleDateUtils.daysOfDuration(first.getCreateTime(), now) > 20) {
                        costAndCompanyDto.setCostTypeEnum(CostTypeEnum.CONSTRUCTION);
                        costAndCompanyDto.setCompanyId(first.getCompanyId());
                    } else {
                        costAndCompanyDto.setCostTypeEnum(CostTypeEnum.MATERIAL);
                        costAndCompanyDto.setCompanyId(first.getCompanyId());
                    }
                } else {
                    costAndCompanyDto.setCostTypeEnum(CostTypeEnum.CONSTRUCTION);
                    costAndCompanyDto.setCompanyId(first.getCompanyId());
                }
            }
        } else {
            costAndCompanyDto.setCostTypeEnum(CostTypeEnum.DESIGN);
            costAndCompanyDto.setCompanyId(getCompanyId());
        }
        return costAndCompanyDto;
    }

    /**
     * 随机获取公司id TODO 规则待修改
     *
     * @return 分配的公司id
     */
    private long getCompanyId() {
        /*List<Long> companyIds = posDao.getCompanyIds();
        long companyId = 0;
        if (!CollectionUtils.isEmpty(companyIds)) companyId = companyIds.get(new Random().nextInt(companyIds.size()));
        return companyId;*/
        return new Random().nextInt(100);
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

        PosTransaction record = posDao.queryRecordById(recordId);
        if (record == null) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_RECORD_NOT_EXISTED);
        }
        TransactionStatusType statusType = TransactionStatusType.getEnum(record.getStatus());
        if (!TransactionStatusType.TRANSACTION_FAILED.equals(statusType)) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_STATUS_ERROR);
        }
        PosTransactionHandled saveInfo = new PosTransactionHandled();
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
        PosTransaction record = posDao.queryRecordById(recordId);
        if (record == null) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_RECORD_NOT_EXISTED);
        }
        TransactionStatusType statusType = TransactionStatusType.getEnum(record.getStatus());
        if (!TransactionStatusType.TRANSACTION_FAILED.equals(statusType)) {
            return ApiResult.fail(PosUserErrorCode.TRANSACTION_STATUS_ERROR);
        }
        // 重发提现请求
        SettlementCardWithdrawVo settlement = buildSettlementCardWithdrawVo(record, record.getArrivalAmount());
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
            // 发起提现失败，更新交易状态-交易失败
            log.error("失败交易{}重发提现请求失败。原因：{}", recordId, withdrawResult.getMessage());
            return ApiResult.fail(withdrawResult.getError(), withdrawResult.getMessage());
        }

        return ApiResult.succ();
    }
}
