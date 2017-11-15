/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类.
 *
 * @author wayne
 * @version 1.0, 2016/6/12
 */
public class MD5Utils {

    private static final Logger LOG = LoggerFactory.getLogger(MD5Utils.class);

    private MD5Utils() {
    }

    public static String getMD5Code(String str) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
            StringBuilder code = new StringBuilder(bytes.length << 1);
            for (int a = 0; a < bytes.length; ++a) {
                code.append(Character.forDigit((bytes[a] >> 4) & 0xf, 16));
                code.append(Character.forDigit(bytes[a] & 0xf, 16));
            }
            return code.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOG.error("生成MD5失败！", ex);
            return null;
        }
    }

}