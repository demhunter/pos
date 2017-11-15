/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * POS 相关配置信息
 *
 * @author wangbing
 * @version 1.0, 2017/10/16
 */
@Component
public class PosConstants {

    // 默认提现金额上下限（10-20000）
    private final static BigDecimal defaultPosAmountDownLimit = new BigDecimal("10");
    private final static BigDecimal defaultPosAmountUpLimit = new BigDecimal("20000");

    @Value("${pos.head.image.url}")
    private String posHeadImage; // POS用户默认头像地址

    @Value("${pos.poundage.rate}")
    private String posPoundageRate; // 收款功能默认费率

    @Value("${pos.poundage.rate.down.limit}")
    private String posPoundageRateDownLimit; // 收款功能费率下限

    @Value("${pos.twitter.poundage.rate}")
    private String posTwitterPoundageRate; // 拥有推客权限的收款用户的收款功能默认费率

    @Value("${pos.parent.twitter.brokerage.rate}")
    private String posParentTwitterBrokerageRate;

    @Value("${pos.extra.poundage}")
    private String posExtraPoundage; // 收款额外手续费

    @Value("${pos.arrival}")
    private String posArrival; // 快捷收款到账时间

    @Value("${pos.amount.down.limit}")
    private String posAmountDownLimit; // 提现金额下限

    @Value("${pos.amount.up.limit}")
    private String posAmountUpLimit; // 提现金额上限

    @Value("${pos.helibao.merchant.no}")
    private String helibaoMerchantNO; // 合利宝的商户号

    @Value("${pos.helibao.notify.url}")
    private String helibaoCallbackUrl; // 合利宝回调地址

    @Value("${pos.helibao.poundage.rate}")
    private String helibaoPoundageRate; // 提现时合利宝收取的手续费比例

    @Value("${pos.helibao.same.person.signkey}")
    private String helibaoSameSignKey; // 请求参数签名时的MD5的密钥  即快捷产品的密钥

    @Value("${pos.helibao.transfer.url}")
    private String helibaoTransferUrl; // 合利宝支付的请求地址

    @Value("${pos.helibao.tixian.rate}")
    private String helibaoTixianRate; // 公司支付钱给用户时合利宝收取的手续费比例

    @Value("${pos.helibao.tixian.money}")
    private String helibaoTixianMoney; // 公司支付钱给用户时合利宝收取的手续费

    @Value("${pos.admin.userName}")
    private String posAdminUserName; // 管理员临时固定账号

    @Value("${pos.admin.password}")
    private String posAdminPassword; // 管理员临时固定密码

    @Value("${pos.wechat.appid}")
    private String posWeChatAppId; // POS 微信公众号appid

    @Value("${pos.wechat.secret}")
    private String posWeChatSecret; // POS 微信公众号secret

    @Value("${pos.twitter.brokerage.handled.template}")
    private String posTwitterBrokerageHandledTemplate; // POS 提现申请已处理短信模板

    @Value("${pos.twitter.status.enable.template}")
    private String posTwitterStatusEnableTemplate; // POS 开通推客身份短信模板

    @Value("${pos.twitter.status.disable.template}")
    private String posTwitterStatusDisableTemplate; // POS 关闭推客身份短信模板

    @Value("${pos.user.get.rate.changed.template}")
    private String posUserGetRateChangedTemplate; // POS 用户收款费率变更短信模板

    @Value("${qiniu.bucket.image.address}")
    private String qiniuBucketImageAddress; // 七牛图片空间地址

    public BigDecimal getHelibaoPoundageRate() {
        return StringUtils.isEmpty(helibaoPoundageRate) ? BigDecimal.ZERO : new BigDecimal(helibaoPoundageRate);
    }

    public String getPosHeadImage() {
        return posHeadImage;
    }

    public BigDecimal getPosPoundageRate() {
        return StringUtils.isEmpty(posPoundageRate) ? BigDecimal.ZERO : new BigDecimal(posPoundageRate);
    }

    public BigDecimal getPosPoundageRateDownLimit() {
        return StringUtils.isEmpty(posPoundageRateDownLimit) ? BigDecimal.ZERO : new BigDecimal(posPoundageRateDownLimit);
    }

    public BigDecimal getPosTwitterPoundageRate() {
        return StringUtils.isEmpty(posTwitterPoundageRate) ? BigDecimal.ZERO : new BigDecimal(posTwitterPoundageRate);
    }

    public BigDecimal getPosExtraPoundage() {
        return StringUtils.isEmpty(posExtraPoundage) ? BigDecimal.ZERO : new BigDecimal(posExtraPoundage);
    }

    public String getPosArrival() {
        return posArrival;
    }

    public BigDecimal getPosAmountDownLimit() {
        return StringUtils.isEmpty(posAmountDownLimit) ? defaultPosAmountDownLimit : new BigDecimal(posAmountDownLimit);
    }

    public BigDecimal getPosAmountUpLimit() {
        return StringUtils.isEmpty(posAmountUpLimit) ? defaultPosAmountUpLimit : new BigDecimal(posAmountUpLimit);
    }

    public String getHelibaoMerchantNO() {
        return helibaoMerchantNO;
    }

    public String getHelibaoCallbackUrl() {
        return helibaoCallbackUrl;
    }

    public String getHelibaoSameSignKey() {
        return helibaoSameSignKey;
    }

    public String getHelibaoTransferUrl() {
        return helibaoTransferUrl;
    }

    public BigDecimal getHelibaoTixianRate() {
        return StringUtils.isEmpty(helibaoTixianRate) ? BigDecimal.ZERO : new BigDecimal(helibaoTixianRate);
    }

    public BigDecimal getHelibaoTixianMoney() {
        return StringUtils.isEmpty(helibaoTixianMoney) ? BigDecimal.ZERO : new BigDecimal(helibaoTixianMoney);
    }

    public BigDecimal getPosParentTwitterBrokerageRate() {
        return StringUtils.isEmpty(posParentTwitterBrokerageRate) ? BigDecimal.ZERO : new BigDecimal(posParentTwitterBrokerageRate);
    }

    public String getPosAdminUserName() {
        return posAdminUserName;
    }

    public String getPosAdminPassword() {
        return posAdminPassword;
    }

    public String getPosWeChatAppId() {
        return posWeChatAppId;
    }

    public String getPosWeChatSecret() {
        return posWeChatSecret;
    }

    public String getPosTwitterBrokerageHandledTemplate() {
        return posTwitterBrokerageHandledTemplate;
    }

    public String getPosTwitterStatusEnableTemplate() {
        return posTwitterStatusEnableTemplate;
    }

    public String getPosTwitterStatusDisableTemplate() {
        return posTwitterStatusDisableTemplate;
    }

    public String getPosUserGetRateChangedTemplate() {
        return posUserGetRateChangedTemplate;
    }

    public String getQiniuBucketImageAddress() {
        return qiniuBucketImageAddress;
    }
}
