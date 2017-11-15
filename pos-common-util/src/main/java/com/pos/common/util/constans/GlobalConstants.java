package com.pos.common.util.constans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取配置文件的类  将properties文件的属性读取 然后程序中直接使用此类的属性 增加复用性 不用在类里面再注入
 * Created by 睿智 on 2017/6/13.
 */
@Component
public class GlobalConstants {

    //阿里支付的折扣比例
    @Value("${pay.alipay.discount.ratio}")
    public String aliPayRatio;

    //微信支付的折扣比例
    @Value("${pay.wechat.discount.ratio}")
    public String wechatPayRatio;

    //易宝支付的折扣比例
    @Value("${pay.yibao.discount.ratio}")
    public String yibaoRatio;

    //四方监管协议模版
    @Value("${supervisor.template.url}")
    public String supervisorTemplateURL;

    //POS的功能说明
    @Value("${pos.explain.url}")
    public String explainUrl;

    //公司支付钱给用户时合利宝收取的手续费
    @Value("${pos.helibao.tixian.money}")
    public String helibaoTixianMoney;

    //公司支付钱给用户时 渠道商的佣金比例
    @Value("${pos.helibao.tixian.leader}")
    public String tixianLeader;

    //POS的合利宝回调地址
    @Value("${pos.helibao.notify.url}")
    public String helibaoCallbackUrl;

    //合利宝的商户号
    @Value("${pos.helibao.merchant.no}")
    public String helibaoMerchantNO;

    //合利宝的同人的请求URL
    @Value("${pos.helibao.same.person.url}")
    public String helibaoSameUrl;

    //请求参数签名时的MD5的密钥  即快捷产品的密钥
    @Value("${pos.helibao.same.person.signkey}")
    public String helibaoSameSignKey;

    //合利宝支付的请求地址
    @Value("${pos.helibao.transfer.url}")
    public String helibaoTransferUrl;

    //合利宝支付请求的私钥 RSA算法使用
    @Value("${pos.helibao.transfer.key}")
    public String helibaoTransferKey;

    //合利宝支付请求的MD5的签名密钥 即代付产品的密钥
    @Value("${pos.helibao.transfer.sign}")
    public String helibaoTransferSign;

    //POS 用户默认头像地址
    @Value("${pos.head.image.url}")
    public String posHeadImage;

    //网易云信的appSecret
    @Value("${im.netease.app.secret}")
    public String imNeteaseAppSecret;

    //网易云信的appkey
    @Value("${im.netease.app.key}")
    public String imNeteaseAppKey;


    @Value("${pay.app.yop.tradeOrderURI}")
    public String tradeorderUrl;

    @Value("${pay.app.yop.parentMerchantNo}")
    public String parentMerchantNo;

    @Value("${pay.app.yop.merchantNo}")
    public String merchatNo;

    @Value("${pay.app.yop.CASHIER}")
    public String cashierUrl;

    @Value("${pay.app.yop.baseURL}")
    public String baseUrl;
}
