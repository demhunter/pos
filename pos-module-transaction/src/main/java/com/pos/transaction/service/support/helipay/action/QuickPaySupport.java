/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support.helipay.action;

import com.alibaba.fastjson.JSONObject;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.transaction.constants.PosConstants;
import com.pos.transaction.helipay.util.Disguiser;
import com.pos.transaction.helipay.util.HttpClientService;
import com.pos.transaction.helipay.util.MyBeanUtils;
import com.pos.transaction.helipay.util.PosErrorCode;
import com.pos.transaction.helipay.vo.SettlementCardBindResponseVo;
import com.pos.transaction.service.support.helipay.dto.settlement.card.bind.SettlementCardBindDto;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 合利宝同人进出支付支持类
 *
 * @author wangbing
 * @version 1.0, 2017/12/15
 */
@Component
@SuppressWarnings("all")
public class QuickPaySupport {

    private static final Logger LOG = LoggerFactory.getLogger(QuickPaySupport.class);

    private static final String SPLIT = "&";

    @Resource
    private PosConstants posConstants;

    @Resource
    private GlobalConstants globalConstants;


    public ApiResult<SettlementCardBindResponseVo> settlementCardBind(SettlementCardBindDto bindDto) {
        LOG.info("--------进入绑结算卡接口----------");
        try {
            String [] signExcludes = {"P11_operateType"};
            Map<String, String> map = MyBeanUtils.convertBean(bindDto, new LinkedHashMap());
            String oriMessage = MyBeanUtils.getSigned(map, signExcludes,SPLIT,globalConstants.helibaoSameSignKey);
            LOG.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            LOG.info("签名串：" + sign);
            map.put("sign", sign);
            LOG.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, globalConstants.helibaoSameUrl);
            LOG.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SettlementCardBindResponseVo responseVo = JSONObject.parseObject(resultMsg, SettlementCardBindResponseVo.class);
                String[] excludes = {"rt3_retMsg"};
                String assemblyRespOriSign = MyBeanUtils.getSigned(responseVo, excludes,SPLIT,globalConstants.helibaoSameSignKey);
                LOG.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = responseVo.getSign();
                LOG.info("响应签名：" + responseSign);
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
}
