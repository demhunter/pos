/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.condition.query.ManagerListCondition;
import com.pos.user.dto.manager.ManagerDto;

/**
 * 平台管理员相关服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
public interface ManagerService {

    /**
     * 根据用户ID查询平台管理员信息.
     *
     * @param userId  用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    ManagerDto findById(Long userId, boolean disable, boolean deleted);

    /**
     * 根据用户名查询平台管理员信息.
     *
     * @param userName 用户名
     * @param disable  是否返回被禁用的账号
     * @param deleted  是否返回被删除的账号
     */
    ManagerDto findByUserName(String userName, boolean disable, boolean deleted);

    /**
     * 根据用户ID查询平台管理员信息.
     *
     * @param userPhone 手机号
     * @param disable   是否返回被禁用的账号
     * @param deleted   是否返回被删除的账号
     */
    ManagerDto findByUserPhone(String userPhone, boolean disable, boolean deleted);

    /**
     * 更新平台管理员信息, 仅当数据有修改时才执行update.
     * <p>
     * 注意: 未设置的属性值在更新后将被覆盖！建议先调用findById等相关方法以填充数据并拷贝修改前的对象.
     * </p>
     * <i>example:</i>
     * <pre>
     *     ApiResult<ManagerDto> result = service.findById(id, disable, deleted); // 查询以填充数据
     *     ManagerDto beforeDto = result.getData().copy(); // 修改前先拷贝对象
     *     result.getData().setXXX(...); // 开始修改数据
     *     ...
     *     service.update(beforeDto, result.getData()); // 修改完毕调用更新方法
     * </pre>
     *
     * @param beforeDto 修改前的用户数据
     * @param afterDto  修改后的用户数据
     * @return 数据有被更新返回true, 否则返回false
     */
    boolean update(ManagerDto beforeDto, ManagerDto afterDto);

    /**
     * 查询指定条件的平台管理员列表.
     *
     * @param condition   公共查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     */
    ApiResult<Pagination<ManagerDto>> findManagers(
            ManagerListCondition condition, LimitHelper limitHelper, OrderHelper orderHelper);

}
