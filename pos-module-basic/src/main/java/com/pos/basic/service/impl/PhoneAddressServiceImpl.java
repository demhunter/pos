/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.service.PhoneAddressService;
import com.pos.basic.dao.AreaDao;
import com.pos.basic.domain.Area;
import com.pos.basic.dto.phoneAddress.PhoneAddressResultDto;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.web.http.HttpRequestUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机号码归属地查询ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/7/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PhoneAddressServiceImpl implements PhoneAddressService {

    private final static Logger logger = LoggerFactory.getLogger(PhoneAddressServiceImpl.class);

    @Value("${phone.address.query.url}")
    private String queryBaseUrl;

    @Value("${phone.address.query.app.code}")
    private String appCode;

    private final static String REQUEST_METHOD_GET = "GET";

    private final static Integer SUCCESS_CODE = 0; // 成功返回码

    @Resource
    private AreaDao areaDao;

    @Override
    public Long queryPhoneAddress(String phoneNum) {
        FieldChecker.checkEmpty(phoneNum, "phoneNum");
        if (!SimpleRegexUtils.isMobile(phoneNum)) {
            // 1、不是手机号码，直接返回
            return null;
        }

        // 2、构建查询header和拼接好的url
        String queryUrl = queryBaseUrl + "?num=" + phoneNum;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "APPCODE " + appCode);
        headerMap.put("content-type", "application/x-www-form-urlencoded");

        // 3、发起HTTP请求
        String resultStr;
        try {
            resultStr = HttpRequestUtils.httpRequest(queryUrl, REQUEST_METHOD_GET, headerMap, null);
        } catch (Exception e) {
            logger.error("手机号码归属查询失败，原因：" + e.toString());
            return null;
        }

        // 4、解析返回结果
        if (!StringUtils.isEmpty(resultStr)) {
            logger.info(resultStr);
            PhoneAddressResultDto addressInfo = JsonUtils.jsonToObject(resultStr, new TypeReference<PhoneAddressResultDto>() {});
            if (SUCCESS_CODE.equals(addressInfo.getShowapi_res_code())) {
                if (SUCCESS_CODE.equals(addressInfo.getShowapi_res_body().getRet_code())) {
                    // 5、根据解析结果查询数据库城市信息
                    Area area = areaDao.getCityInfo(addressInfo.getShowapi_res_body().getCity());
                    if (area != null) {
                        // 6、查询成功，返回城市id，否则返回null
                        return area.getId();
                    }
                } else {
                    logger.warn("手机号码归属查询失败，原因：" + addressInfo.getShowapi_res_error());
                }
            } else {
                logger.warn("手机号码归属查询失败，原因：" + addressInfo.getShowapi_res_error());
            }
        }

        return null;
    }
}
