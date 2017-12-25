package com.pos.transaction.helipay.action;

import com.alibaba.fastjson.JSONObject;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.helipay.util.*;
import com.pos.transaction.helipay.vo.*;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class QuickPayApi {

    private static final Log log = LogFactory.getLog(QuickPayApi.class);

    public static final String SPLIT = "&";

    @Resource
    private GlobalConstants globalConstants;

    @Resource
    private PosConstants posConstants;

    public ApiResult<CreateOrderResponseVo> createOrder(CreateOrderVo orderVo) {
        log.info("--------进入创建订单接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(orderVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null, SPLIT, globalConstants.helibaoSameSignKey);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                CreateOrderResponseVo orderResponseVo = JSONObject.parseObject(resultMsg, CreateOrderResponseVo.class);
                String[] excludes = {"rt3_retMsg"};
                String assemblyRespOriSign = MyBeanUtils.getSigned(orderResponseVo, excludes, SPLIT, globalConstants.helibaoSameSignKey);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = orderResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(orderResponseVo.getRt2_retCode())) {
                        return ApiResult.succ(orderResponseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, orderResponseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }

    public ApiResult<SendValidateCodeResponseVo> sendValidateCode(SendValidateCodeVo sendVo) {
        log.info("--------进入发送短信接口----------");
        try {
            String[] requestExcludes = {"P6_smsSignature"};
            Map<String, String> map = MyBeanUtils.convertBean(sendVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, requestExcludes, SPLIT, globalConstants.helibaoSameSignKey);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SendValidateCodeResponseVo sendResponseVo = JSONObject.parseObject(resultMsg, SendValidateCodeResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(sendResponseVo, null, SPLIT, globalConstants.helibaoSameSignKey);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = sendResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(sendResponseVo.getRt2_retCode())) {
                        return ApiResult.succ(sendResponseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, sendResponseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "请求异常：" + e.getMessage());
        }
    }

    public ApiResult<ConfirmPayResponseVo> confirmPay(ConfirmPayVo confirmPayVo) {
        log.info("--------进入确认支付接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(confirmPayVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null, SPLIT, globalConstants.helibaoSameSignKey);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                ConfirmPayResponseVo confirmPayResponseVo = JSONObject.parseObject(resultMsg, ConfirmPayResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(confirmPayResponseVo, null, SPLIT, globalConstants.helibaoSameSignKey);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = confirmPayResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(confirmPayResponseVo.getRt2_retCode())) {
                        return ApiResult.succ(confirmPayResponseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, confirmPayResponseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }

    public ApiResult<BindCardPayResponseVo> bindCardPay(BindCardPayVo bindCardPayVo) {
        log.info("--------进入绑卡支付接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(bindCardPayVo, new LinkedHashMap());
            String[] requestExcludes = {"P17_validateCode"};
            String oriMessage = MyBeanUtils.getSigned(map, requestExcludes, SPLIT, globalConstants.helibaoSameSignKey);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                BindCardPayResponseVo bindCardPayResponseVo = JSONObject.parseObject(resultMsg, BindCardPayResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(bindCardPayResponseVo, null, SPLIT, globalConstants.helibaoSameSignKey);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = bindCardPayResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(bindCardPayResponseVo.getRt2_retCode())) {
                        return ApiResult.succ(bindCardPayResponseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, bindCardPayResponseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }

    public ApiResult<QueryOrderResponseVo> queryOrder(QueryOrderVo queryOrderVo) {
        log.info("--------进入订单查询接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(queryOrderVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, null, SPLIT, globalConstants.helibaoSameSignKey);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                QueryOrderResponseVo queryOrderResponseVo = JSONObject.parseObject(resultMsg, QueryOrderResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(queryOrderResponseVo, null, SPLIT, globalConstants.helibaoSameSignKey);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = queryOrderResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(queryOrderResponseVo.getRt2_retCode())) {
                        return ApiResult.succ(queryOrderResponseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, queryOrderResponseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }


    public ApiResult<SettlementCardBindResponseVo> settlementCardBind(SettlementCardBindVo settlementCardBindVo) {
        log.info("--------进入绑结算卡接口----------");
        try {
            String[] signExcludes = {"P11_operateType"};
            Map<String, String> map = MyBeanUtils.convertBean(settlementCardBindVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, signExcludes, SPLIT, globalConstants.helibaoSameSignKey);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SettlementCardBindResponseVo responseVo = JSONObject.parseObject(resultMsg, SettlementCardBindResponseVo.class);
                String[] excludes = {"rt3_retMsg"};
                String assemblyRespOriSign = MyBeanUtils.getSigned(responseVo, excludes, SPLIT, globalConstants.helibaoSameSignKey);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = responseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(responseVo.getRt2_retCode())) {
                        return ApiResult.succ(responseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, responseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }


    public ApiResult<SettlementCardWithdrawResponseVo> settlementCardWithdraw(SettlementCardWithdrawVo settlementCardWithdrawVo) {
        log.info("--------进入结算卡提现接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(settlementCardWithdrawVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(settlementCardWithdrawVo, null, SPLIT, globalConstants.helibaoSameSignKey);
            oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf(SPLIT));
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage, RSA.getPrivateKey(globalConstants.helibaoTransferKey));
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoTransferUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SettlementCardWithdrawResponseVo responseVo = JSONObject.parseObject(resultMsg, SettlementCardWithdrawResponseVo.class);
                String[] excludes = {"rt3_retMsg"};
                String assemblyRespOriSign = MyBeanUtils.getSigned(responseVo, excludes, SPLIT, globalConstants.helibaoSameSignKey);
                assemblyRespOriSign = assemblyRespOriSign.substring(0, assemblyRespOriSign.lastIndexOf(SPLIT) + 1);
                assemblyRespOriSign += globalConstants.helibaoTransferSign;
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = responseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名串：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(responseVo.getRt2_retCode())) {
                        return ApiResult.succ(responseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, responseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }

    public ApiResult<QuerySettlementCardVo> querySettlementCardWithdraw(QueryOrderVo queryOrderVo) {
        log.info("--------进入结算卡提现查询接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(queryOrderVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(queryOrderVo, null, SPLIT, globalConstants.helibaoSameSignKey);
            oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf(SPLIT));
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage, RSA.getPrivateKey(globalConstants.helibaoTransferKey));
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoTransferUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                QuerySettlementCardVo querySettlementVo = JSONObject.parseObject(resultMsg, QuerySettlementCardVo.class);
                String[] excludes = {"rt3_retMsg"};
                String assemblyRespOriSign = MyBeanUtils.getSigned(querySettlementVo, excludes, SPLIT, posConstants.getHelibaoSameSignKey());
                assemblyRespOriSign = assemblyRespOriSign.substring(0, assemblyRespOriSign.lastIndexOf(SPLIT) + 1);
                assemblyRespOriSign += globalConstants.helibaoTransferSign;
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = querySettlementVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名串：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(querySettlementVo.getRt2_retCode())) {
                        return ApiResult.succ(querySettlementVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, querySettlementVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }

    public ApiResult<MerchantWithdrawResponseVo> merchantWithdraw(MerchantWithdrawVo merchantWithdrawVo) {
        log.info("--------进入商户提现接口----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(merchantWithdrawVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(merchantWithdrawVo, null, SPLIT, globalConstants.helibaoSameSignKey);
            oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf(SPLIT));
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage, RSA.getPrivateKey(globalConstants.helibaoTransferKey));
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoTransferUrl);
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                MerchantWithdrawResponseVo responseVo = JSONObject.parseObject(resultMsg, MerchantWithdrawResponseVo.class);
                String assemblyRespOriSign = MyBeanUtils.getSigned(responseVo, null, SPLIT, globalConstants.helibaoSameSignKey);
                assemblyRespOriSign = assemblyRespOriSign.substring(0, assemblyRespOriSign.lastIndexOf(SPLIT) + 1);
                assemblyRespOriSign += globalConstants.helibaoTransferSign;
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = responseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名串：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(responseVo.getRt2_retCode())) {
                        return ApiResult.succ(responseVo);
                    } else {
                        return ApiResult.failFormatMsg(PosErrorCode.POS_NORMAL_FAIL, responseVo.getRt3_retMsg());
                    }
                } else {
                    return ApiResult.fail(PosErrorCode.CHECK_SIGN_FAIL, "验签失败");
                }
            } else {
                return ApiResult.fail(PosErrorCode.REQUEST_FAIL, "请求失败");
            }
        } catch (Exception e) {
            return ApiResult.fail(PosErrorCode.PAY_EXCEPTION, "交易异常：" + e.getMessage());
        }
    }


    public static void main(String[] args) {
        String orderId = UUID.randomUUID().toString();
        System.out.println("orderId ========== " + orderId);
//        SettlementCardBindVo settlementCardBindVo = new SettlementCardBindVo();
//        settlementCardBindVo.setP1_bizType("SettlementCardBind");
//        settlementCardBindVo.setP2_customerNumber(MERCHANT_NO);
//        settlementCardBindVo.setP3_userId("2");
//        settlementCardBindVo.setP4_orderId(orderId);
//        settlementCardBindVo.setP5_payerName("王兵");
//        settlementCardBindVo.setP6_idCardType("IDCARD");
//        settlementCardBindVo.setP7_idCardNo("511111199501203938");
//        settlementCardBindVo.setP8_cardNo("6214832801194965");
//        settlementCardBindVo.setP9_phone("18328724719");
//        ApiResult<SettlementCardBindResponseVo> apiResult = settlementCardBind(settlementCardBindVo);
//        if(apiResult.isSucc()) {
//            System.out.println("success:"+apiResult.getData().getRt3_retMsg());
//        }else{
//            System.out.println("fail:"+apiResult.getMessage());
//        }
       /* SettlementCardWithdrawVo settlementCardWithdrawVo = new SettlementCardWithdrawVo();
        settlementCardWithdrawVo.setP1_bizType("SettlementCardWithdraw");
        settlementCardWithdrawVo.setP2_customerNumber("C1800001401");
        settlementCardWithdrawVo.setP3_userId("1036");
        settlementCardWithdrawVo.setP4_orderId(orderId);
        settlementCardWithdrawVo.setP5_amount("3300");
        settlementCardWithdrawVo.setP6_feeType("PAYER");
        log.info("--------进入结算卡提现----------");
        try {
            Map<String, String> map = MyBeanUtils.convertBean(settlementCardWithdrawVo, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(settlementCardWithdrawVo, null,SPLIT,"6oRQOzdnCtQJ52hHHHPpjN0ukr3zMuCt");
            oriMessage = oriMessage.substring(0, oriMessage.lastIndexOf(SPLIT));
            log.info("签名原文串：" + oriMessage);
            String sign = RSA.sign(oriMessage, RSA.getPrivateKey("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK9thtNWHy/cLxjyNDKmiewc+ftyCMdB9b+KmZ2XlLYCVhtY2cToDOYHnIqJLcY2PVei1k+CEqOYoRSwdWtxbIXOh9rGJjVjmJTHcXsXFCqEBQQdmEKKhsjGkQf4NL7J/dHgatKxmBUUkzkU9PbEVnYVuahI78upD1Jop0uB2GYfAgMBAAECgYB8M8+BUThDamDuCI1sTvzXbqyOme4dJVYYhsi8CtX/ByhvtDh6cNCxDDKI4xbFfyFvKpsRL8aCjU1+mHCJ4YQzC5wAQ596X1kPLBYK6MvlZFSshmQwaFmqkVFF9FGEEnTtZiDxKwRf+GgFaUKNKMSEoKfMo6Vr1EynI4UFgkWuAQJBAOGWb/9FdFzTmEw8lb/II7mASH4tTIS35oXvy4VZmGtIx4Y1t+mz+viP+r+yoalnm2i1vwWDiKAUiDlznttncMsCQQDHE/SqdFzAZQne7fjEohsBSEJb4gH1goJTA5+jztVPj/cisOpW9KMhnkSX86Sq5tXhYfCdiosaUrzakQU9J5l9AkAFAT9u3G2eeZtRZa602I3iWbRCCGNANoxIwG81gC1fg/fZRGvWJYYV6avYgPARQBk0k4OvbaGkW5BCJgyKNZtNAkBCuAFrjwv2vuYL/J0+6UU7rMfwm1IkwdSDlddOwubif1FIIxqmgd6aSbybYGBzlmFf478MTX5JGCmK5sdms3rRAkA7ABWuwCmsNu+ytVb7NiDGh+YjDAUMLugoyID9SBUZnVNggh2Q4RlUqkCd2sXrjujvjp618M0Z4Ulw9cYkdllE"));
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientUtil.getHttpResp(map, "http://transfer.trx.helipay.com/trx/transfer/interface.action");
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SettlementCardWithdrawResponseVo responseVo = JSONObject.parseObject(resultMsg, SettlementCardWithdrawResponseVo.class);
                String[] excludes = {"rt3_retMsg"};
                String assemblyRespOriSign = MyBeanUtils.getSigned(responseVo, excludes,SPLIT,"6oRQOzdnCtQJ52hHHHPpjN0ukr3zMuCt");
                assemblyRespOriSign = assemblyRespOriSign.substring(0, assemblyRespOriSign.lastIndexOf(SPLIT) + 1);
                assemblyRespOriSign += "6oRQOzdnCtQJ52hHHHPpjN0ukr3zMuCt";
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = responseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名串：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(responseVo.getRt2_retCode())) {
                        System.out.println("请求成功");
                    } else {
                        System.out.println("失败："+responseVo.getRt3_retMsg());
                    }
                } else {
                    System.out.println("验签失败");
                }
            } else {
                System.out.println("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("交易异常：" + e.getMessage());
        }*/
    }

}
