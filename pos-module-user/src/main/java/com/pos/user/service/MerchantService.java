/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.user.dto.merchant.MerchantDto;

/**
 * 商家管理员相关服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
public interface MerchantService {

    /**
     * 根据用户ID查询商家管理员信息.
     *
     * @param userId  用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    MerchantDto findById(Long userId, boolean disable, boolean deleted);

    /**
     * 根据用户名查询商家管理员信息.
     *
     * @param userName 用户名
     * @param disable  是否返回被禁用的账号
     * @param deleted  是否返回被删除的账号
     */
    MerchantDto findByUserName(String userName, boolean disable, boolean deleted);

    /**
     * 根据用户手机号查询商家管理员信息.
     *
     * @param userPhone 手机号
     * @param disable   是否返回被禁用的账号
     * @param deleted   是否返回被删除的账号
     */
    MerchantDto findByUserPhone(String userPhone, boolean disable, boolean deleted);

    /**
     * 更新商家管理员信息, 仅当数据有修改时才执行update.
     * <p>
     * 注意: 未设置的属性值在更新后将被覆盖！建议先调用findById等相关方法以填充数据并拷贝修改前的对象.
     * </p>
     * <i>example:</i>
     * <pre>
     *     ApiResult<MerchantDto> result = service.findById(id, disable, deleted); // 查询以填充数据
     *     MerchantDto beforeDto = result.getData().copy(); // 修改前先拷贝对象
     *     result.getData().setXXX(...); // 开始修改数据
     *     ...
     *     service.update(beforeDto, result.getData()); // 修改完毕调用更新方法
     * </pre>
     *
     * @param beforeDto 修改前的用户数据
     * @param afterDto 修改后的用户数据
     * @return 数据有被更新返回true, 否则返回false
     */
    boolean update(MerchantDto beforeDto, MerchantDto afterDto);

}