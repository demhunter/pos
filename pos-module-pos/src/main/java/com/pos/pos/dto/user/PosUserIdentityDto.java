/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dto.user;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.basic.service.SecurityService;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;

/**
 * POS 用户身份认证信息
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
public class PosUserIdentityDto implements Serializable {

    private static final long serialVersionUID = 5673447466214520189L;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("当身份认证未通过时，真实姓名是否允许修改，false = 不允许修改")
    private Boolean realNameCanModify;

    @ApiModelProperty("身份证号码")
    private String idCardNo;

    @ApiModelProperty("当身份认证未通过时，身份证号码是否允许修改，false = 不允许修改")
    private Boolean idCardNoCanModify;

    @ApiModelProperty("身份证正面照")
    private String idImageA;

    @ApiModelProperty("身份证反面照")
    private String idImageB;

    @ApiModelProperty("身份证正面持证照")
    private String idHoldImage;

    @ApiModelProperty("当身份认证未通过时，三种照片是否允许修改，false = 不允许修改")
    private Boolean imageCanModify;

    /**
     * 更新是检查字段信息是否符合要求
     *
     * @param fieldPrefix 提示前缀
     * @param securityService 解密服务（姓名和身份证号为加密数据，校验之前需要解密）
     * @throws IllegalParamException 参数异常
     */
    public void check(String fieldPrefix, SecurityService securityService) throws IllegalParamException {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";

        FieldChecker.checkEmpty(this.realName, fieldPrefix + "realName");
        String realName = securityService.decryptData(this.realName);
        FieldChecker.checkMinMaxLength(realName, 2, 10, fieldPrefix + "realName");

        FieldChecker.checkEmpty(this.idCardNo, fieldPrefix + "idCardNo");
        String idCardNo = securityService.decryptData(this.idCardNo);
        SimpleRegexUtils.checkIdNumber(idCardNo);

        FieldChecker.checkEmpty(this.idImageA, fieldPrefix + "idImageA");
        FieldChecker.checkEmpty(this.idImageB, fieldPrefix + "idImageB");
        FieldChecker.checkEmpty(this.idHoldImage, fieldPrefix + "idHoldImage");
    }

    public Boolean getRealNameCanModify() {
        return realNameCanModify;
    }

    public void setRealNameCanModify(Boolean realNameCanModify) {
        this.realNameCanModify = realNameCanModify;
    }

    public Boolean getIdCardNoCanModify() {
        return idCardNoCanModify;
    }

    public void setIdCardNoCanModify(Boolean idCardNoCanModify) {
        this.idCardNoCanModify = idCardNoCanModify;
    }

    public Boolean getImageCanModify() {
        return imageCanModify;
    }

    public void setImageCanModify(Boolean imageCanModify) {
        this.imageCanModify = imageCanModify;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdImageA() {
        return idImageA;
    }

    public void setIdImageA(String idImageA) {
        this.idImageA = idImageA;
    }

    public String getIdImageB() {
        return idImageB;
    }

    public void setIdImageB(String idImageB) {
        this.idImageB = idImageB;
    }

    public String getIdHoldImage() {
        return idHoldImage;
    }

    public void setIdHoldImage(String idHoldImage) {
        this.idHoldImage = idHoldImage;
    }
}
