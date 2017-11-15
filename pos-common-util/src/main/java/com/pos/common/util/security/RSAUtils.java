/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.security;

import com.pos.common.util.codec.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * from internet & modified by cc
 */
@SuppressWarnings("all")
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encryptBASE64(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decryptBASE64(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decryptBASE64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decryptBASE64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encryptBASE64(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encryptBASE64(key.getEncoded());
    }

    /**
     * Demo
     *
     * @param args args
     * @throws Exception Ex
     */
    public static void main(String[] args) throws Exception {
        /*String encryptedPassword = "co5MufBb+DF+8iMNRHkYgcYGGaBiFjztROlquumG91A608uu7JhBT6BFJkTqc3qu+EOJ6aYN23PAx7WvjTBTT6zZNk92kSDJh4h2tgeM7Mi0mkB/WImFX+0NhoLBL3FI6sV9owh1meAB1dVJg0vqNK13zVAu420tFeZx3+paQ8U=";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIEbdQU8DGH5rQ0u/o+W+Ojlg8N2eVsQPx0jgY/bUN0Uq9dUVw9vSVJGuG3leuDtyTg8n9+i99KLuTwxio9jnaD22c/OQC2FU6z31PDoNV7ba9XaFyzt1e+2T5PErLgioyNkDvuGIfzAxhwm6v6jhHvvSatU/h8/zzHncRDg9prJAgMBAAECgYAK2sod5IyN+DXqc9cHL4RB4HoRhvZxB46m3oNYGvJThBdmhTrEm3CosDV1V+Pa4WMXjVFLtiHr2795JhkmMRPhgwjJOO7w35SrzsxY1wKjoYsFCUfqLOB2SAVOg4JSMhW+tdvBWDKn4zlgDqmy8a6Uy759ZcuXhYA1psQYsIF5AQJBAOsv65P8C0ARqUs0OTxlK3wsQy58XqHDLiMRxrradv6QzWMwu0XVL7PkmytZY7xVouNx+gqraazgd+xOggooIdECQQCMiFWyx0F31B23ldzG4LYDCh2q6Pp9wcRu7ZWedSkOJRG5Y7lUpl+vzqyPspxTXcBQW/XaffMhZPZtiJyQRW95AkAYJAATXZCuD+IHtSGW4G+ZPFXdBKkWA5nNwbpbXadPM//RCaR/Y4WU+ocu6OsC3utsWzumMrgTJatJlzlj34CxAkAVvt7r7BNAVI1IpCLmj0z6yWzvzl88aGhZ9d+KBn0U2D2W30yFQb1aufNPxQaVi9M/XAt+BLFDgJj1OAdp96SZAkBhYn5HlnYKr1moGkMJ9DBGNW0lpPEke0GhwHfpWOQO1/WySjvRLaVPsyfGtHsZcg1fVYUwUd84tOmlYKLe1YnQ";
        byte[] decodedData = RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(encryptedPassword), privateKey);
        System.out.println(new String(decodedData));*/

        /*Map<String, Object> keyMap = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(keyMap);
        publicKey = publicKey.replaceAll("\\n", "");
        String privateKey = RSAUtils.getPrivateKey(keyMap);
        privateKey = privateKey.replaceAll("\\n", "");

        String source = "hi, there";
        byte[] encodedData = RSAUtils.encryptByPublicKey(source.getBytes(), publicKey);
        String encodedDataStr = Base64Utils.encryptBASE64(encodedData);
        byte[] decodedData = RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(encodedDataStr), privateKey);
        System.out.println(new String(decodedData));*/

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBG3UFPAxh+a0NLv6Plvjo5YPDdnlbED8dI4GP21DdFKvXVFcPb0lSRrht5Xrg7ck4PJ/fovfSi7k8MYqPY52g9tnPzkAthVOs99Tw6DVe22vV2hcs7dXvtk+TxKy4IqMjZA77hiH8wMYcJur+o4R770mrVP4fP88x53EQ4PaayQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIEbdQU8DGH5rQ0u/o+W+Ojlg8N2eVsQPx0jgY/bUN0Uq9dUVw9vSVJGuG3leuDtyTg8n9+i99KLuTwxio9jnaD22c/OQC2FU6z31PDoNV7ba9XaFyzt1e+2T5PErLgioyNkDvuGIfzAxhwm6v6jhHvvSatU/h8/zzHncRDg9prJAgMBAAECgYAK2sod5IyN+DXqc9cHL4RB4HoRhvZxB46m3oNYGvJThBdmhTrEm3CosDV1V+Pa4WMXjVFLtiHr2795JhkmMRPhgwjJOO7w35SrzsxY1wKjoYsFCUfqLOB2SAVOg4JSMhW+tdvBWDKn4zlgDqmy8a6Uy759ZcuXhYA1psQYsIF5AQJBAOsv65P8C0ARqUs0OTxlK3wsQy58XqHDLiMRxrradv6QzWMwu0XVL7PkmytZY7xVouNx+gqraazgd+xOggooIdECQQCMiFWyx0F31B23ldzG4LYDCh2q6Pp9wcRu7ZWedSkOJRG5Y7lUpl+vzqyPspxTXcBQW/XaffMhZPZtiJyQRW95AkAYJAATXZCuD+IHtSGW4G+ZPFXdBKkWA5nNwbpbXadPM//RCaR/Y4WU+ocu6OsC3utsWzumMrgTJatJlzlj34CxAkAVvt7r7BNAVI1IpCLmj0z6yWzvzl88aGhZ9d+KBn0U2D2W30yFQb1aufNPxQaVi9M/XAt+BLFDgJj1OAdp96SZAkBhYn5HlnYKr1moGkMJ9DBGNW0lpPEke0GhwHfpWOQO1/WySjvRLaVPsyfGtHsZcg1fVYUwUd84tOmlYKLe1YnQ";

//        byte[] data = RSAUtils.encryptByPublicKey("何加明".getBytes(), publicKey);
//        String str = Base64Utils.encryptBASE64(data).replaceAll("\\n", "");
//        System.out.println(str);
//        String decryptStr = new String(RSAUtils.decryptByPrivateKey("KbCa013knyWsbAPrChGNZvLq7Ds5ZxUJ6ZYgALkEljwqKxnqjZhs03d6uUfsbHxsgwIGtiJkkojPUcJthc+KRRTq49yAdJRPean4al8eBmur5ldE/kFZurpqe9Yvsryq2ZRFG67eXPAzDhtng2NfETmjzHYnGWwEdNlLJpfgxnY=".getBytes(),privateKey));

        /*byte [] data = encryptByPrivateKey("何家明".getBytes(),privateKey);
        String str = Base64Utils.encryptBASE64(data).replaceAll("\\n","");
        String base64 = new Base64().encodeBase64String(data);
        System.out.println("str========="+str);
        System.out.println("base64============"+base64);
        System.out.println(new String(Base64Utils.decryptBASE64(str)));
        System.out.println(new String(RSAUtils.decryptByPublicKey(new Base64().decodeBase64(base64),publicKey)));
        String decryptStr = new String(RSAUtils.decryptByPublicKey(data,publicKey));
        System.out.println("解密出来的是："+decryptStr);

        byte [] pubData = encryptByPublicKey("何家明".getBytes(),publicKey);
        System.out.println(Base64Utils.encryptBASE64(pubData).replaceAll("\\n",""));
        System.out.println(new Base64().encodeBase64String(pubData));*/

        String decryptStr = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64("Qm8+EfxJo54RuBVXVlaenlB5lfBZaQiIC94MwDGutyAkKCbsZh6eSjPjHGDi6PAriMzDXP/WXIUd0RK5Rv32Hm52Oc5JRMXe9Wh2uP2I8KQrLtwtYda62k7OplBa1tclqcwvMbVL4XsY4ueE/nog/wwDxKzqrIqQbLqAoft/0Ak="), privateKey));
        System.out.println(decryptStr);
        System.out.println(URLDecoder.decode(decryptStr,"UTF-8"));


        /*String str = "QCCe4OToinGDLXvZfWQANm4VV+isBoe1RYGlxW6TpS41pPwAGdFf3mAylO7lSBf4ammTiJmlFuDwDH5w8y7VFxo87x4QrjxUS00sYhTzT4GT1kRim+/NE7CzQsjuk7pXFAYbG+xfLgiMmYQlVV8DXXiopvf7TmWeVggyZ/fgfBc=";
        byte[] decodedData = RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(str), privateKey);
        System.out.println(new String(decodedData));*/
    }
}