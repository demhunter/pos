/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author 睿智
 * @version 1.0, 2017/9/13
 */
public class SHAUtils {

    /**
     * SHA1 安全加密算法
     *
     * @param maps 参数key-value map集合
     * @return
     * @throws DigestException
     */
    public static String SHA1(Map<String, Object> maps) throws DigestException {
        //获取信息摘要 - 参数字典排序后字符串
        String decrypt = getOrderByLexicographic(maps);
        return SHA1(decrypt);
    }

    public static String SHA1(String text)  throws DigestException{
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(text.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigestException("签名错误！");
        }
    }

    /**
     * 获取参数的字典排序
     *
     * @param maps 参数key-value map集合
     * @return String 排序后的字符串
     */
    private static String getOrderByLexicographic(Map<String, Object> maps) {
        return splitParams(lexicographicOrder(getParamsName(maps)), maps);
    }

    /**
     * 获取参数名称 key
     *
     * @param maps 参数key-value map集合
     * @return
     */
    private static List<String> getParamsName(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getKey());
        }
        return paramNames;
    }

    /**
     * 参数名称按字典排序
     *
     * @param paramNames 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    private static List<String> lexicographicOrder(List<String> paramNames) {
        Collections.sort(paramNames);
        return paramNames;
    }

    /**
     * 拼接排序好的参数名称和参数值
     *
     * @param paramNames 排序后的参数名称集合
     * @param maps       参数key-value map集合
     * @return String 拼接后的字符串
     */
    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder paramStr = new StringBuilder();
        for (String paramName : paramNames) {
            if(paramStr.length()!=0){
                paramStr.append("&");
            }
            paramStr.append(paramName);
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                if (paramName.equals(entry.getKey())) {
                    paramStr.append("=").append(String.valueOf(entry.getValue()));
                }
            }
        }
        return paramStr.toString();
    }

    public static void main(String[] args) {
        String s = "jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value";
        try {
            System.out.println("SHA1加密的字符串："+SHA1(s));
        } catch (DigestException e) {
            e.printStackTrace();
        }

        Map<String,Object> params = new HashMap<>();
        params.put("noncestr","Wm3WZYTPz0wzccnW");
        params.put("jsapi_ticket","sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg");
        params.put("url","http://mp.weixin.qq.com?params=value");
        params.put("timestamp","1414587457");
        try {
            System.out.println(SHA1(params));
        } catch (DigestException e) {
            e.printStackTrace();
        }

    }
}
