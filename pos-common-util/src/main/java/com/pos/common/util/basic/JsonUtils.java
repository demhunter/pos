/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.pos.common.util.exception.IllegalParamException;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json工具类
 *
 * Created by cc on 16/6/19.
 */
public class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    /**
     * json转换为对象
     *
     * @param str json
     * @param typeRef 类型
     * @param <T> 范型
     * @return 对象
     * @throws IllegalParamException str传null时会抛出异常！！
     */
    public static <T> T jsonToObject(String str, TypeReference<T> typeRef) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalParamException("非法的参数");
        }

        T result;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            result = objectMapper.readValue(str, typeRef);
        } catch (IOException e) {
            LOG.error("JSON反序列化异常，str={}", str, e);
            throw new IllegalStateException("数据错误");
        }

        return result;
    }

    public static String objectToJson(Object obj) {
        String result;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            result = objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("JSON序列化异常，obj={}", obj);
            return StringUtils.EMPTY;
        }

        return result;
    }

    public static String objectToJsonIgnoreNull(Object obj) {
        String result;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            result = objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("JSON序列化异常，obj={}", obj);
            return StringUtils.EMPTY;
        }

        return result;
    }
}
