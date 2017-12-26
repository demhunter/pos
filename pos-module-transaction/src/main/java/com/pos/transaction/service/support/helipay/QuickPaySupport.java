/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.helipay.util.PosErrorCode;
import com.pos.transaction.helipay.util.RSA;
import com.pos.transaction.service.support.helipay.dto.HelibaoBasicResponse;
import com.pos.transaction.service.support.helipay.dto.order.code.OrderValidateCodeDto;
import com.pos.transaction.service.support.helipay.dto.order.code.OrderValidateCodeResponseDto;
import com.pos.transaction.service.support.helipay.dto.order.create.OrderCreateDto;
import com.pos.transaction.service.support.helipay.dto.order.create.OrderCreateResponseDto;
import com.pos.transaction.service.support.helipay.dto.order.pay.OrderConfirmPayDto;
import com.pos.transaction.service.support.helipay.dto.order.pay.OrderConfirmPayResponseDto;
import com.pos.transaction.service.support.helipay.dto.order.query.OrderQueryDto;
import com.pos.transaction.service.support.helipay.dto.order.query.OrderQueryResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.bind.SettlementCardBindDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.bind.SettlementCardBindResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.query.SettlementCardQueryDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.query.SettlementCardQueryResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.withdraw.SettlementWithdrawDto;
import com.pos.transaction.service.support.helipay.dto.settlement.withdraw.SettlementWithdrawResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.withdraw.merchant.MerchantWithdrawDto;
import com.pos.transaction.service.support.helipay.dto.settlement.withdraw.merchant.MerchantWithdrawResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.withdraw.query.SettlementWithdrawQueryDto;
import com.pos.transaction.service.support.helipay.dto.settlement.withdraw.query.SettlementWithdrawQueryResponseDto;
import com.pos.transaction.service.support.helipay.util.Disguiser;
import com.pos.transaction.service.support.helipay.util.HttpClientUtil;
import com.pos.transaction.service.support.helipay.util.MyBeanUtils;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 合利宝同人进出支付支持类
 *
 * @author wangbing
 * @version 1.0, 2017/12/15
 */
public class QuickPaySupport {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPaySupport.class);

    private static final String SPLIT = "&";

    private static final String RESULT_SUCCESS_CODE = "0000"; // 操作成功返回码

    @Resource
    private PosConstants posConstants;

    /**
     * 绑定结算银行卡
     *
     * @param bindDto 绑定信息
     * @return 绑定结果
     */
    public ApiResult<SettlementCardBindResponseDto> bindSettlementCard(SettlementCardBindDto bindDto) {
        FieldChecker.checkEmpty(bindDto, "bindDto");
        LOG.info("--------进入绑结算卡接口----------");
        String[] signExcludes = {"P11_operateType"};
        try {
            Map<String, String> map = buildRequestData(bindDto, signExcludes);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoSamePersonUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                String[] excludes = {"rt3_retMsg"};
                return (ApiResult<SettlementCardBindResponseDto>) extractResponse(
                        resultMsg, new TypeReference<SettlementCardBindResponseDto>() {
                        }, excludes);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("绑定结算银行卡异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            // 响应超时，主动查询绑卡结果
            SettlementCardQueryDto queryDto = new SettlementCardQueryDto();
            queryDto.setP2_customerNumber(bindDto.getP2_customerNumber());
            queryDto.setP3_userId(bindDto.getP3_userId());
            queryDto.setP4_orderId(bindDto.getP4_orderId());
            ApiResult<SettlementCardQueryResponseDto> queryResult = querySettlementCard(queryDto);
            if (queryResult.isSucc()) {
                SettlementCardQueryResponseDto queryResponse = queryResult.getData();
                SettlementCardBindResponseDto bindResult = new SettlementCardBindResponseDto();
                bindResult.setRt1_bizType("SettlementCardWithdraw");
                bindResult.setRt2_retCode(queryResponse.getRt2_retCode());
                bindResult.setRt3_retMsg(queryResponse.getRt3_retMsg());
                bindResult.setRt4_customerNumber(queryResponse.getRt4_customerNumber());
                bindResult.setRt5_userId(queryResponse.getRt5_userId());
                bindResult.setRt6_orderId(bindDto.getP4_orderId());
                bindResult.setRt7_bindStatus("SUCCESS");
                bindResult.setRt8_bankId(queryResponse.getRt10_bankCode());
                String cardAfterFour = StringUtils.substring(queryResponse.getRt9_cardNo(), queryResponse.getRt9_cardNo().length() - 4);
                bindResult.setRt9_cardAfterFour(cardAfterFour);

                return ApiResult.succ(bindResult);
            } else {
                // 查询失败，返回绑卡响应超时
                LOG.error("绑定结算银行卡异常-请求响应超时，exception = {}", sTimeout);
                return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
            }
        } catch (Exception e) {
            LOG.error("绑定结算银行卡异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.SETTLEMENT_CARD_BIND_EXCEPTION);
        }
    }


    /**
     * 查询用户绑定的结算银行卡
     *
     * @param queryDto 查询信息
     * @return 查询结果
     */
    public ApiResult<SettlementCardQueryResponseDto> querySettlementCard(SettlementCardQueryDto queryDto) {
        FieldChecker.checkEmpty(queryDto, "queryDto");
        LOG.info("--------进入查询结算银行卡接口----------");
        try {
            Map<String, String> map = buildRequestData(queryDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoSamePersonUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                return (ApiResult<SettlementCardQueryResponseDto>) extractResponse(
                        resultMsg, new TypeReference<SettlementCardQueryResponseDto>() {
                        }, null);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("结算卡查询异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            LOG.error("结算卡查询异常-请求响应超时，exception = {}", sTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
        } catch (Exception e) {
            LOG.error("结算卡查询异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.SETTLEMENT_CARD_QUERY_EXCEPTION);
        }
    }

    /**
     * 支付下单
     *
     * @param createDto 下单请求信息
     * @return 下单请求结果
     */
    public ApiResult<OrderCreateResponseDto> createOrder(OrderCreateDto createDto) {
        LOG.info("--------进入创建订单接口----------");
        try {
            Map<String, String> map = buildRequestData(createDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoSamePersonUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");

                String[] excludes = {"rt3_retMsg"};
                return (ApiResult<OrderCreateResponseDto>) extractResponse(
                        resultMsg, new TypeReference<OrderCreateResponseDto>() {
                        }, excludes);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("支付下单异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            LOG.error("支付下单异常-请求响应超时，exception = {}", sTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
        } catch (Exception e) {
            LOG.error("支付下单异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.SETTLEMENT_CARD_QUERY_EXCEPTION);
        }
    }

    /**
     * 发送支付验证码短信
     *
     * @param codeDto 支付验证码发送请求信息
     * @return 发送结果
     */
    public ApiResult<OrderValidateCodeResponseDto> sendValidateCode(OrderValidateCodeDto codeDto) {
        LOG.info("--------进入发送短信接口----------");
        try {
            String[] requestExcludes = {"P6_smsSignature"};
            Map<String, String> map = buildRequestData(codeDto, requestExcludes);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoSamePersonUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                return (ApiResult<OrderValidateCodeResponseDto>) extractResponse(
                        resultMsg, new TypeReference<OrderValidateCodeResponseDto>() {
                        }, null);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("发送支付验证码异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            LOG.error("发送支付验证码异常-请求响应超时，exception = {}", sTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
        } catch (Exception e) {
            LOG.error("发送支付验证码异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.VALIDATE_CODE_SEND_EXCEPTION);
        }
    }

    /**
     * 确认支付
     *
     * @param confirmPayDto 确认支付信息
     * @return 确认支付结果
     */
    public ApiResult<OrderConfirmPayResponseDto> confirmPay(OrderConfirmPayDto confirmPayDto) {
        LOG.info("--------进入确认支付接口----------");
        try {
            Map<String, String> map = buildRequestData(confirmPayDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoSamePersonUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                return (ApiResult<OrderConfirmPayResponseDto>) extractResponse(
                        resultMsg, new TypeReference<OrderConfirmPayResponseDto>() {
                        }, null);
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("确认支付异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            // 确认支付响应超时，主动查询确认支付结果
            LOG.warn("确认支付响应超时，主动查询确认支付结果");
            OrderQueryDto queryDto = new OrderQueryDto(confirmPayDto.getP3_orderId());
            queryDto.setP3_customerNumber(confirmPayDto.getP2_customerNumber());
            ApiResult<OrderQueryResponseDto> queryResult = queryOrder(queryDto);
            if (queryResult.isSucc()) {
                LOG.warn("获取到查询支付结果，组装返回结果");
                OrderQueryResponseDto responseDto = queryResult.getData();
                OrderConfirmPayResponseDto confirmPayResult = new OrderConfirmPayResponseDto();
                confirmPayResult.setRt1_bizType("QuickPayConfirmPay");
                confirmPayResult.setRt2_retCode(responseDto.getRt2_retCode());
                confirmPayResult.setRt3_retMsg(responseDto.getRt3_retMsg());
                confirmPayResult.setRt4_customerNumber(responseDto.getRt4_customerNumber());
                confirmPayResult.setRt5_orderId(responseDto.getRt5_orderId());
                confirmPayResult.setRt6_serialNumber(responseDto.getRt10_serialNumber());
                confirmPayResult.setRt7_completeDate(responseDto.getRt8_completeDate());
                confirmPayResult.setRt8_orderAmount(responseDto.getRt6_orderAmount());
                confirmPayResult.setRt9_orderStatus(responseDto.getRt9_orderStatus());
                confirmPayResult.setRt10_bindId(responseDto.getRt14_bindId());
                confirmPayResult.setRt11_bankId(responseDto.getRt11_bankId());
                confirmPayResult.setRt12_onlineCardType(responseDto.getRt12_onlineCardType());
                confirmPayResult.setRt13_cardAfterFour(responseDto.getRt13_cardAfterFour());
                confirmPayResult.setRt14_userId(responseDto.getRt15_userId());

                return ApiResult.succ(confirmPayResult);
            } else {
                LOG.error("确认支付异常-请求响应超时，exception = {}", sTimeout);
                return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
            }
        } catch (Exception e) {
            LOG.error("确认支付异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.ORDER_CONFIRM_PAY_EXCEPTION);
        }
    }

    /**
     * 订单信息查询
     *
     * @param queryDto 查询信息
     * @return 查询结果
     */
    public ApiResult<OrderQueryResponseDto> queryOrder(OrderQueryDto queryDto) {
        LOG.info("--------进入订单查询接口----------");
        try {
            Map<String, String> map = buildRequestData(queryDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoSamePersonUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                return (ApiResult<OrderQueryResponseDto>) extractResponse(
                        resultMsg, new TypeReference<OrderQueryResponseDto>() {
                        }, null);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("订单信息查询异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            LOG.error("订单信息查询异常-请求响应超时，exception = {}", sTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
        } catch (Exception e) {
            LOG.error("订单信息查询异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.ORDER_QUERY_EXCEPTION);
        }
    }

    /**
     * 提现到用户结算卡
     *
     * @param settlementWithdrawDto 结算提现请求信息
     * @return 结算提现返回信息
     */
    public ApiResult<SettlementWithdrawResponseDto> settlementCardWithdraw(SettlementWithdrawDto settlementWithdrawDto) {
        LOG.info("--------进入结算卡提现接口----------");
        try {
            Map<String, String> map = buildTransferRequestData(settlementWithdrawDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoTransferUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                String[] excludes = {"rt3_retMsg"};
                return (ApiResult<SettlementWithdrawResponseDto>) extractTransferResponse(
                        resultMsg, new TypeReference<HelibaoBasicResponse>() {
                        }, excludes);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("提现到用户结算卡异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            // 提现响应超时，主动查询提现结果
            LOG.warn("提现响应超时，主动查询提现结果");
            SettlementWithdrawQueryDto queryDto = new SettlementWithdrawQueryDto(settlementWithdrawDto.getP4_orderId());
            queryDto.setP3_customerNumber(settlementWithdrawDto.getP2_customerNumber());
            ApiResult<SettlementWithdrawQueryResponseDto> queryResult = querySettlementCardWithdraw(queryDto);
            if (queryResult.isSucc()) {
                LOG.warn("查询到结算提现结果");
                SettlementWithdrawQueryResponseDto queryResponseDto = queryResult.getData();
                SettlementWithdrawResponseDto result = new SettlementWithdrawResponseDto();
                result.setRt1_bizType("SettlementCardWithdraw");
                result.setRt2_retCode(queryResponseDto.getRt2_retCode());
                result.setRt3_retMsg(queryResponseDto.getRt3_retMsg());
                result.setRt4_customerNumber(queryResponseDto.getRt4_customerNumber());
                result.setRt5_userId(settlementWithdrawDto.getP3_userId());
                result.setRt6_orderId(queryResponseDto.getRt5_orderId());
                result.setRt7_serialNumber(queryResponseDto.getRt6_serialNumber());

                return ApiResult.succ(result);
            } else {
                LOG.error("提现到用户结算卡异常-请求响应超时，exception = {}", sTimeout);
                return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
            }
        } catch (Exception e) {
            LOG.error("提现到用户结算卡异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.SETTLEMENT_WITHDRAW_EXCEPTION);
        }
    }

    /**
     * 结算提现结果查询
     *
     * @param settlementWithdrawQueryDto 查询请求信息
     * @return 查询结果
     */
    public ApiResult<SettlementWithdrawQueryResponseDto> querySettlementCardWithdraw(SettlementWithdrawQueryDto settlementWithdrawQueryDto) {
        LOG.info("--------进入结算卡提现查询接口----------");
        try {
            Map<String, String> map = buildTransferRequestData(settlementWithdrawQueryDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoTransferUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                String[] excludes = {"rt3_retMsg"};

                return (ApiResult<SettlementWithdrawQueryResponseDto>) extractTransferResponse(
                        resultMsg, new TypeReference<SettlementWithdrawQueryResponseDto>() {
                        }, excludes);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("结算提现查询异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            LOG.error("结算提现查询异常-请求响应超时，exception = {}", sTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
        } catch (Exception e) {
            LOG.error("结算提现查询异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.SETTLEMENT_WITHDRAW_QUERY_EXCEPTION);
        }
    }

    /**
     * 商户提现<br>
     * 注意：此接口是从商户总账上面直接提取到指定用户银行卡上
     *
     * @param merchantWithdrawDto 商户提现信息
     * @return 提现结果
     */
    public ApiResult<MerchantWithdrawResponseDto> merchantWithdraw(MerchantWithdrawDto merchantWithdrawDto) {
        LOG.info("--------进入商户提现接口----------");
        try {
            Map<String, String> map = buildTransferRequestData(merchantWithdrawDto, null);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, posConstants.getHelibaoTransferUrl());
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                return (ApiResult<MerchantWithdrawResponseDto>) extractTransferResponse(
                        resultMsg, new TypeReference<HelibaoBasicResponse>() {
                        }, null);
            } else {
                return ApiResult.fail(HelibaoErrorCode.REQUEST_FAIL);
            }
        } catch (ConnectTimeoutException cTimeout) {
            LOG.error("商户提现异常-请求超时，exception = {}", cTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_REQUEST_TIMEOUT);
        } catch (SocketTimeoutException sTimeout) {
            LOG.error("商户提现异常-请求响应超时，exception = {}", sTimeout);
            return ApiResult.fail(CommonErrorCode.HTTP_RESPONSE_TIMEOUT);
        } catch (Exception e) {
            LOG.error("商户提现异常，exception = {}", e);
            return ApiResult.fail(HelibaoErrorCode.SETTLEMENT_MERCHANT_WITHDRAW_EXCEPTION);
        }
    }

    // 提取响应数据
    private ApiResult<? extends HelibaoBasicResponse> extractResponse(String data, TypeReference<? extends HelibaoBasicResponse> reference, String[] signExcludes)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        HelibaoBasicResponse response = JsonUtils.jsonToObject(data, reference);
        // 校验响应数据签名
        if (checkResponse(response, signExcludes)) {
            if (RESULT_SUCCESS_CODE.equals(response.getRt2_retCode())) {
                return ApiResult.succ(response);
            } else {
                return ApiResult.failFormatMsg(HelibaoErrorCode.NORMAL_FAIL_MSG, response.getRt3_retMsg());
            }
        } else {
            return ApiResult.fail(HelibaoErrorCode.SIGN_CHECKED_FAIL);
        }
    }

    // 构建请求传递数据
    private Map<String, String> buildRequestData(Object data, String[] signExcludes)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String, String> map = MyBeanUtils.convertBean(data, new LinkedHashMap());
        // 签名请求数据
        String sign = getSign(map, signExcludes);
        LOG.info("签名串：" + sign);
        map.put("sign", sign);

        return map;
    }

    // 构建Transafer请求传递数据
    private Map<String, String> buildTransferRequestData(Object data, String[] signExcludes)
            throws Exception {
        Map<String, String> map = MyBeanUtils.convertBean(data, new LinkedHashMap());
        String oriMessage = MyBeanUtils.getSigned(data, null, SPLIT, posConstants.getHelibaoSameSignKey());
        oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf(SPLIT));
        LOG.info("签名原文串：" + oriMessage);
        String sign = RSA.sign(oriMessage, RSA.getPrivateKey(posConstants.getHelibaoTransferKey()));
        LOG.info("签名串：" + sign);
        map.put("sign", sign);

        return map;
    }

    // 获取Transfer数据签名
    private String getTransferSign(Map<String, String> map, String[] signExcludes) {
        String oriMessage = MyBeanUtils.getSigned(map, signExcludes, SPLIT, posConstants.getHelibaoSameSignKey());
        LOG.info("签名原文串：" + oriMessage);
        return Disguiser.disguiseMD5(oriMessage.trim());
    }

    // 获取数据签名
    private String getSign(Map<String, String> map, String[] signExcludes) {
        String oriMessage = MyBeanUtils.getSigned(map, signExcludes, SPLIT, posConstants.getHelibaoSameSignKey());
        LOG.info("签名原文串：" + oriMessage);
        return Disguiser.disguiseMD5(oriMessage.trim());
    }

    // 获取数据签名
    private String getSign(Object data, String[] signExcludes)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String, String> map = MyBeanUtils.convertBean(data, new LinkedHashMap());
        return getSign(map, signExcludes);
    }

    // 校验返回数据
    private boolean checkResponse(HelibaoBasicResponse response, String[] signExcludes)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        String responseSign = response.getSign();
        LOG.info("响应签名：" + responseSign);
        String checkSign = getSign(response, signExcludes);
        LOG.info("验证签名串：" + checkSign);
        return checkSign.equals(responseSign);
    }

    // 校验Transfer请求地址的响应结果
    private boolean checkTransferResponse(HelibaoBasicResponse response, String[] signExcludes)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        String assemblyRespOriSign = MyBeanUtils.getSigned(response, signExcludes, SPLIT, posConstants.getHelibaoSameSignKey());
        assemblyRespOriSign = assemblyRespOriSign.substring(0, assemblyRespOriSign.lastIndexOf(SPLIT) + 1);
        assemblyRespOriSign += posConstants.getHelibaoTransferSign();
        LOG.info("组装返回结果签名串：" + assemblyRespOriSign);
        String responseSign = response.getSign();
        LOG.info("响应签名：" + responseSign);
        String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
        LOG.info("验证签名串：" + checkSign);

        return checkSign.equals(responseSign);
    }

    // 提取Transfer响应数据
    private ApiResult<? extends HelibaoBasicResponse> extractTransferResponse(String data, TypeReference<? extends HelibaoBasicResponse> reference, String[] signExcludes)
            throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        HelibaoBasicResponse response = JsonUtils.jsonToObject(data, reference);
        // 校验响应数据签名
        if (checkTransferResponse(response, signExcludes)) {
            if (RESULT_SUCCESS_CODE.equals(response.getRt2_retCode())) {
                return ApiResult.succ(response);
            } else {
                return ApiResult.failFormatMsg(HelibaoErrorCode.NORMAL_FAIL_MSG, response.getRt3_retMsg());
            }
        } else {
            return ApiResult.fail(HelibaoErrorCode.SIGN_CHECKED_FAIL);
        }
    }
}
