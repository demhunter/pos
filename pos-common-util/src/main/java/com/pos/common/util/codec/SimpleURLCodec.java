/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * 简单的URL编码&解码器.
 *
 * @author cc
 * @version 1.0, 16/8/25
 */
public class SimpleURLCodec {

    private static final Logger logger = LoggerFactory.getLogger(SimpleURLCodec.class);

    public static final String UTF_8 = "UTF-8";

    public static final String GBK = "GBK";

    public static String encode(String str, String enc) {
        String encodedStr;
        try {
            encodedStr = URLEncoder.encode(str, enc);
        } catch (UnsupportedEncodingException e) {
            logger.error("URL编码错误", e);
            throw new IllegalStateException("URL编码错误");
        }

        return encodedStr;
    }

    public static String decode(String str, String dec) {
        String decodedStr;
        try {
            decodedStr = URLDecoder.decode(str, dec);
        } catch (UnsupportedEncodingException e) {
            logger.error("URL解码错误", e);
            throw new IllegalStateException("URL解码错误");
        }

        return decodedStr;
    }

    public static String encodeUtf8(String str) {
        return encode(str, UTF_8);
    }

    public static String decodeUtf8(String str) {
        return decode(str, UTF_8);
    }

    public static String encodeGBK(String str) {
        return encode(str, GBK);
    }

    public static String decodeGBK(String str) {
        return decode(str, GBK);
    }

    /**
     * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode   是否需要URL_ENCODE
     * @param keyToLower    是否需要将Key转换为全小写 true:key转化成小写，false:不转化
     * @return r
     */
    public static String formatUrl(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
    {
        if(paraMap == null){
            return "";
        }
        String buff;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(paraMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
            {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
                {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds)
            {
                String key = item.getKey();
                String val = item.getValue();
                if (urlEncode)
                {
                    val = URLEncoder.encode(val, "utf-8");
                }
                if (keyToLower)
                {
                    buf.append(key.toLowerCase() + "=" + val);
                } else
                {
                    buf.append(key + "=" + val);
                }
                buf.append("&");
            }
            buff = buf.toString();
            if (!buff.equals(""))
            {
                // 去除最后一个'&'符
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }
}
