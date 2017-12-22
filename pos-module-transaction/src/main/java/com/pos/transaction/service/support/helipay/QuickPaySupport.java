/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay;

import com.alibaba.fastjson.JSONObject;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.helipay.util.HttpClientService;
import com.pos.transaction.helipay.util.PosErrorCode;
import com.pos.transaction.helipay.vo.QueryOrderResponseVo;
import com.pos.transaction.service.support.helipay.dto.HelibaoBasicResponse;
import com.pos.transaction.service.support.helipay.dto.order.create.OrderCreateDto;
import com.pos.transaction.service.support.helipay.dto.order.create.OrderCreateResponseDto;
import com.pos.transaction.service.support.helipay.dto.order.query.OrderQueryDto;
import com.pos.transaction.service.support.helipay.dto.order.query.OrderQueryResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.bind.SettlementCardBindDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.bind.SettlementCardBindResponseDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.query.SettlementCardQueryDto;
import com.pos.transaction.service.support.helipay.dto.settlement.card.query.SettlementCardQueryResponseDto;
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
@Component
public class QuickPaySupport {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPaySupport.class);

    private static final String SPLIT = "&";

    private static final String RESULT_SUCCESS_CODE = "0000"; // 操作成功返回码

    @Resource
    private PosConstants posConstants;

    @Resource
    private GlobalConstants globalConstants;


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
                SettlementCardBindResponseDto responseDto = JsonUtils.jsonToObject(resultMsg, new TypeReference<SettlementCardBindResponseDto>() {
                });
                String[] excludes = {"rt3_retMsg"};
                if (checkResponse(responseDto, excludes)) {
                    if (RESULT_SUCCESS_CODE.equals(responseDto.getRt2_retCode())) {
                        return ApiResult.succ(responseDto);
                    } else {
                        return ApiResult.failFormatMsg(HelibaoErrorCode.NORMAL_FAIL_MSG, responseDto.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(HelibaoErrorCode.SIGN_CHECKED_FAIL);
                }
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
                SettlementCardQueryResponseDto responseDto = JsonUtils.jsonToObject(resultMsg, new TypeReference<SettlementCardQueryResponseDto>() {
                });
                // 校验签名
                if (checkResponse(responseDto, null)) {
                    if (RESULT_SUCCESS_CODE.equals(responseDto.getRt2_retCode())) {
                        return ApiResult.succ(responseDto);
                    } else {
                        return ApiResult.failFormatMsg(HelibaoErrorCode.NORMAL_FAIL_MSG, responseDto.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(HelibaoErrorCode.SIGN_CHECKED_FAIL);
                }
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
                OrderCreateResponseDto responseDto = JsonUtils.jsonToObject(resultMsg, new TypeReference<OrderCreateResponseDto>() {
                });
                String[] excludes = {"rt3_retMsg"};
                if (checkResponse(responseDto, excludes)) {
                    if (RESULT_SUCCESS_CODE.equals(responseDto.getRt2_retCode())) {
                        return ApiResult.succ(responseDto);
                    } else {
                        return ApiResult.failFormatMsg(HelibaoErrorCode.NORMAL_FAIL_MSG, responseDto.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(HelibaoErrorCode.SIGN_CHECKED_FAIL);
                }
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
            Map<String, Object> resultMap = HttpClientUtil.getHttpRes(map, globalConstants.helibaoSameUrl);
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                OrderQueryResponseDto responseDto = JsonUtils.jsonToObject(resultMsg, new TypeReference<OrderQueryResponseDto>() {
                });
                if (checkResponse(responseDto, null)) {
                    if (RESULT_SUCCESS_CODE.equals(responseDto.getRt2_retCode())) {
                        return ApiResult.succ(responseDto);
                    } else {
                        return ApiResult.failFormatMsg(HelibaoErrorCode.NORMAL_FAIL_MSG, responseDto.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(HelibaoErrorCode.SIGN_CHECKED_FAIL);
                }
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
        LOG.info("校验签名：" + checkSign);
        return checkSign.equals(responseSign);
    }
}
