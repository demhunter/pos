/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

/**
 * 手机号码归属地查询Service
 *
 * @author wangbing
 * @version 1.0, 2017/7/12
 */
public interface PhoneAddressService {

    /**
     * 手机号码归属地查询<br>
     * 查询结果由阿里云的云市场提供<br>
     * 链接地址：https://market.aliyun.com/products/56928004/cmapi014122.html#sku=yuncode812200000<br>
     * 说明：此版本限调用频率1次/每秒。根据手机号码查询其归属地、运营商信息。上亿条数据，囊括最新的170号段。本地如果查不到，会实时从淘宝、百度等大站获取，综合了多个数据源，可以说是最全的归属地。
     * 注意：1、调用频率1次/每秒，不要频繁调用<br>
     *      2、由于免费，但还是需要购买，可以多次购买，但每次购买只有1000次<br>
     *
     * @param phoneNum 待查询手机号码
     * @return 手好号码归属地id（成功返回相应的城市id，失败返回null）
     */
    Long queryPhoneAddress(String phoneNum);
}
