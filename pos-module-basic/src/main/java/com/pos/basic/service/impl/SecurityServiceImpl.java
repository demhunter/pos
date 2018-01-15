/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.service.SecurityService;
import com.pos.common.util.codec.Base64Utils;
import com.pos.common.util.security.RSAUtils;
import com.pos.common.util.validation.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SecurityServiceImpl
 *
 * @author cc
 * @version 1.0, 2016/11/1
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SecurityServiceImpl implements SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Value("${rsa.security.key}")
    private String privateKey;

    @Value("${rsa.public.key}")
    private String publicKey;

    /**
     * 解密数据
     *
     * @param dataStr 加密的数据字符串
     * @return 解密后的数据字符串
     */
    @Override
    public String decryptData(String dataStr) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dataStr), "要解密的数据字符串不能为空！");

        try {
            byte[] decryptedData = RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(dataStr), privateKey);
            return new String(decryptedData,"UTF-8");
        } catch (Exception e) {
            logger.error("解密发生异常！，dataStr={}", dataStr, e);
            throw new IllegalStateException("解密发生异常！");
        }
    }

    /**
     * 加密数据
     *
     * @param dataStr 原数据字符串
     * @return 加密后的数据字符串
     */
    @Override
    public String encryptData(String dataStr) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dataStr), "要加密的数据字符串不能为空！");
        try {
            return Base64Utils.encryptBASE64(RSAUtils.encryptByPublicKey(dataStr.getBytes(), publicKey));
        } catch (Exception e) {
            logger.error("加密发生异常！，dataStr={}", dataStr, e);
            throw new IllegalStateException("加密发生异常！");
        }
    }

    public static void main(String[] args) {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBG3UFPAxh+a0NLv6Plvjo5YPDdnlbED8dI4GP21DdFKvXVFcPb0lSRrht5Xrg7ck4PJ/fovfSi7k8MYqPY52g9tnPzkAthVOs99Tw6DVe22vV2hcs7dXvtk+TxKy4IqMjZA77hiH8wMYcJur+o4R770mrVP4fP88x53EQ4PaayQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIEbdQU8DGH5rQ0u/o+W+Ojlg8N2eVsQPx0jgY/bUN0Uq9dUVw9vSVJGuG3leuDtyTg8n9+i99KLuTwxio9jnaD22c/OQC2FU6z31PDoNV7ba9XaFyzt1e+2T5PErLgioyNkDvuGIfzAxhwm6v6jhHvvSatU/h8/zzHncRDg9prJAgMBAAECgYAK2sod5IyN+DXqc9cHL4RB4HoRhvZxB46m3oNYGvJThBdmhTrEm3CosDV1V+Pa4WMXjVFLtiHr2795JhkmMRPhgwjJOO7w35SrzsxY1wKjoYsFCUfqLOB2SAVOg4JSMhW+tdvBWDKn4zlgDqmy8a6Uy759ZcuXhYA1psQYsIF5AQJBAOsv65P8C0ARqUs0OTxlK3wsQy58XqHDLiMRxrradv6QzWMwu0XVL7PkmytZY7xVouNx+gqraazgd+xOggooIdECQQCMiFWyx0F31B23ldzG4LYDCh2q6Pp9wcRu7ZWedSkOJRG5Y7lUpl+vzqyPspxTXcBQW/XaffMhZPZtiJyQRW95AkAYJAATXZCuD+IHtSGW4G+ZPFXdBKkWA5nNwbpbXadPM//RCaR/Y4WU+ocu6OsC3utsWzumMrgTJatJlzlj34CxAkAVvt7r7BNAVI1IpCLmj0z6yWzvzl88aGhZ9d+KBn0U2D2W30yFQb1aufNPxQaVi9M/XAt+BLFDgJj1OAdp96SZAkBhYn5HlnYKr1moGkMJ9DBGNW0lpPEke0GhwHfpWOQO1/WySjvRLaVPsyfGtHsZcg1fVYUwUd84tOmlYKLe1YnQ";

        String name = "张智";
        String idCardNo = "51012219931210287X";
        String cardNo = "6230524090000265673";
        String phone = "15881260238";
        try {
            System.out.println("name=" + Base64Utils.encryptBASE64(RSAUtils.encryptByPublicKey(name.getBytes(), publicKey)));
            System.out.println("idCardNo=" + Base64Utils.encryptBASE64(RSAUtils.encryptByPublicKey(idCardNo.getBytes(), publicKey)));
            System.out.println("cardNo=" + Base64Utils.encryptBASE64(RSAUtils.encryptByPublicKey(cardNo.getBytes(), publicKey)));
            System.out.println("phone=" + Base64Utils.encryptBASE64(RSAUtils.encryptByPublicKey(phone.getBytes(), publicKey)));
        } catch (Exception e) {
            logger.error("加密发生异常！", e);
            throw new IllegalStateException("加密发生异常！");
        }

        /*String encryptIdCardNo = "NZcSo29/AJDPF8haUkR74QLLWNUNrT6kYql5y7gShyACCPvmn5+Lg8CPyt3rvO8WESnoQdJsaRijYhgPkUG1F1Guzi5X3wyXjhWaYySwuWQLSTKqz7e7G0yNy092OC/DsjQ8nmliRvQlawf4hPNWL4CBEJZ6yeiLvUtxNRjeq24=";
        String encryptName = "Nh5U2Bf/oHQNDRETWr8HnP4lognDBKmY9ejnNtnBM/SoGdn6D1pgcEfeXsPmOnavGG5qA3fgVasjs9gskTiu+kSbBEJ4nWf6HSzLitMNn5OqBV8Zpa1xUKFw26d+GIL0VTvFA+q5kjIK9NfRg6uztHC4ZLTqhSyFh6zdAKtCRRE=";
        String encryptCardNo = "LxxkEHSihpc9bENhT8ufjipTzFKB3bWgG/eLi65WjHh7LgUny5GWiqr22whrp/TOZ6CwabelT4qZS8RQtTbWcbrynjpZ04Ek2xjuRcCq/xAcvBhfnulYSGZngc2SkNjar1vsZaDl/rjhwlNOBsVQnLcsf68EDOe9I+Vdc4WKXn0=";
        try {
            System.out.println("name=" + new String(RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(encryptName), privateKey),"UTF-8"));
            System.out.println("idCardNo=" + new String(RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(encryptIdCardNo), privateKey),"UTF-8"));
            System.out.println("cardNo=" + new String(RSAUtils.decryptByPrivateKey(Base64Utils.decryptBASE64(encryptCardNo), privateKey),"UTF-8"));
        } catch (Exception e) {
            logger.error("解密发生异常！");
            throw new IllegalStateException("解密发生异常！");
        }*/

    }
}
