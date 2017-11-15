/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.pos.common.util.basic.AddressSupport.BaiDuData;
import com.pos.common.util.basic.AddressSupport.TBResultData;
import com.pos.common.util.basic.AddressSupport.ResultData;
import com.pos.common.util.basic.AddressSupport.SinaData;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.web.http.HttpRequestUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 通过Ip转化为地址
 *
 * @author lifei
 * @version 1.0, 2017/9/7
 */
public class IpAddressUtils {

    private final static Logger logger = LoggerFactory.getLogger(IpAddressUtils.class);

    private final static String REQUEST_METHOD_GET = "GET";

    public static String getAddresses(String ip) {
        FieldChecker.checkEmpty(ip, "ip地址");
        String address;
        if (!isIp(ip)) {
            address = "错误IP地址";
            return address;
        }
        if (isInnerIp(ip)) {
            address = "内网IP";
            return address;
        }
        long t = System.currentTimeMillis();
        long seed = t + getIpNum(ip);
        Random random = new Random(seed);
        int key = random.nextInt(3);

        if (key == 0) {
            address = getAddressOfTaoBao(ip);
        } else if (key == 1) {
            address = getAddressOfSina(ip);
        } else {
            address = getAddressOfBaidu(ip);
        }
        return address;
    }

    /**
     * 私有IP：A类  10.0.0.0-10.255.255.255
     * B类  172.16.0.0-172.31.255.255
     * C类  192.168.0.0-192.168.255.255
     * 当然，还有127这个网段是环回地址
     *
     * @param ip IP地址
     * @return 是否是内部IP地址
     */
    private static Boolean isInnerIp(String ip) {
        long ipNum = getIpNum(ip);
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");

        return isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || ip.equals("127.0.0.1");
    }

    private static boolean isInner(long ipNum, long aBegin, long aEnd) {
        return (ipNum >= aBegin) && (ipNum <= aEnd);
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    private static Boolean isIp(String ip) {
        // 定义正则表达式
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        // 判断ip地址是否与正则表达式匹配
        return ip.matches(regex);
    }

    private static String getAddressOfBaidu(String ip) {
        String baseUrl = "http://opendata.baidu.com/api.php?query=";
        String url = baseUrl + ip + "&resource_id=6006" + "&ie=utf8" + "&oe=utf-8" + "&format=json";
        String resultStr = HttpRequestUtils.httpRequestIP(url, REQUEST_METHOD_GET, null);
        String address = null;
        if (resultStr != null) {
            BaiDuData bdData = JsonUtils.jsonToObject(resultStr, new TypeReference<BaiDuData>() {
            });
            if (bdData != null && bdData.getStatus() == 0 && bdData.getData() != null) {
                address = bdData.getData().get(0).getLocation();
            }
        }
        return address;
    }

    private static String getAddressOfSina(String ip) {
        String baseUrl = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
        String url = baseUrl + ip;
        String resultStr = HttpRequestUtils.httpRequestIP(url, REQUEST_METHOD_GET, null);
        String address = null;
        if (resultStr != null) {
            SinaData resultData = JsonUtils.jsonToObject(resultStr, new TypeReference<SinaData>() {
            });
            if (resultData != null && resultData.getRet() == 1) {
                address = resultData.getProvince() + resultData.getCity() + resultData.getDistrict() + resultData.getIsp();
            }
        }
        return address;
    }

    private static String getAddressOfTaoBao(String ip) {
        String baseUrl = "http://ip.taobao.com/service/getIpInfo.php?ip=";
        String url = baseUrl + ip;
        String resultStr = HttpRequestUtils.httpRequestIP(url, REQUEST_METHOD_GET, null);
        String address = null;
        if (resultStr != null) {
            try {
                TBResultData tbData = JsonUtils.jsonToObject(resultStr, new TypeReference<TBResultData>() {
                });
                if (tbData != null && tbData.getCode() == 0 && tbData.getData() != null) {
                    String object = JsonUtils.objectToJson(tbData.getData());
                    ResultData resultData = JsonUtils.jsonToObject(object, new TypeReference<ResultData>(){});
                    address = resultData.getRegion() + resultData.getCounty() + resultData.getCounty() + resultData.getIsp();
                }
            } catch (Exception e) {
                logger.error("数据强制转化失败，data={}，exception={}", resultStr, e);
            }
        }

        return address;
    }
}
