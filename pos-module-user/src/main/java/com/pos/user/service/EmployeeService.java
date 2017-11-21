/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.user.condition.query.CompanyEmployeesCondition;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.employee.EmployeeInfoDto;
import com.pos.user.dto.SessionUserDto;

import java.util.List;

/**
 * 业者相关Service
 *
 * @author cc
 * @version 1.0, 16/8/3
 */
public interface EmployeeService {

    /**
     * 根据用户ID查询业者信息.
     *
     * @param userId  用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    EmployeeDto findById(Long userId, boolean disable, boolean deleted);

    /**
     * 根据用户名查询业者信息.
     *
     * @param userName 用户名
     * @param disable  是否返回被禁用的账号
     * @param deleted  是否返回被删除的账号
     */
    EmployeeDto findByUserName(String userName, boolean disable, boolean deleted);

    /**
     * 根据用户ID查询业者信息.
     *
     * @param userPhone 手机号
     * @param disable   是否返回被禁用的账号
     * @param deleted   是否返回被删除的账号
     */
    EmployeeDto findByUserPhone(String userPhone, boolean disable, boolean deleted);

    /**
     * 根据用户ID查询业者信息.
     *
     * @param invitationCode 设计师主材邀请码
     * @param disable        是否返回被禁用的账号
     * @param deleted        是否返回被删除的账号
     */
    EmployeeDto findByInvitationCode(String invitationCode, boolean disable, boolean deleted);

    /**
     * 根据用户姓名查询业者信息.
     *
     * @param name      姓名(支持首字匹配)
     * @param companyId 所属公司ID(可空)
     * @param maxSize   最大查询条数
     * @param disable   是否返回被禁用的账号
     * @param deleted   是否返回被删除的账号
     */
    List<EmployeeDto> findByName(String name, Long companyId, int maxSize, boolean disable, boolean deleted, boolean quitJob);

    /**
     * 根据用户姓名和类型查询业者信息
     *
     * @param name         推客姓名
     * @param employeeType 业者类型
     * @param disable      是否返回被禁用的账号
     * @param deleted      是否返回被删除的账号
     * @return 业者信息
     */
    List<EmployeeDto> findByNameAndType(String name, Byte employeeType, boolean disable, boolean deleted);

    /**
     * 查找业者 用于IM会话 查找成员
     * @param name
     * @param userPhone
     * @param company
     * @param deleted
     * @param available
     * @return
     */
    List<SessionUserDto> findEmployeeForImMember(String name, String userPhone, String company, Boolean deleted,
                                                 Boolean available, Boolean quitJob);

    /**
     * 根据公司ID查询业者信息.
     *
     * @param companiesId 所属公司ID
     * @param maxSize     最大查询条数
     * @param disable     是否返回被禁用的账号
     * @param deleted     是否返回被删除的账号
     */
    List<EmployeeDto> findByCompaniesId(List<Long> companiesId, int maxSize, boolean disable, boolean deleted, boolean quitJob);

    /**
     * 根据条件查询业者信息（列表）
     *
     * @param condition   查询条件 {@link CompanyEmployeesCondition}
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return 查询结果
     */
    Pagination<EmployeeDto> queryEmployees(CompanyEmployeesCondition condition, LimitHelper limitHelper, OrderHelper
            orderHelper);

    /**
     * 查询雇员数
     *
     * @param condition 查询条件 {@link CompanyEmployeesCondition}
     * @return 查询结果
     */
    int getTotalEmployeeCount(CompanyEmployeesCondition condition);

    /**
     * 更新业者信息, 仅当数据有修改时才执行update.
     * <p>
     * 注意: 未设置的属性值在更新后将被覆盖！建议先调用findById等相关方法以填充数据并拷贝修改前的对象.
     * </p>
     * <i>example:</i>
     * <pre>
     *     ApiResult<EmployeeDto> result = service.findById(id, disable, deleted); // 查询以填充数据
     *     EmployeeDto beforeDto = result.getData().copy(); // 修改前先拷贝对象
     *     result.getData().setXXX(...); // 开始修改数据
     *     ...
     *     service.update(beforeDto, result.getData()); // 修改完毕调用更新方法
     * </pre>
     *
     * @param beforeDto 修改前的用户数据
     * @param afterDto  修改后的用户数据
     * @return 数据有被更新返回true, 否则返回false
     */
    boolean update(EmployeeDto beforeDto, EmployeeDto afterDto);

    /**
     * 查询指定的一组业者信息.
     *
     * @param userIds 一组用户ID
     * @param disable 是否返回被禁用的账号
     * @param deleted 是否返回被删除的账号
     */
    List<EmployeeDto> findInUserIds(List<Long> userIds, boolean disable, boolean deleted);

    /**
     * 通过业者类型和对应的公司查询相关业者
     *
     * @param userType
     * @param companyId
     * @param quitJob
     * @param disable
     * @param deleted
     * @return
     */
    List<EmployeeInfoDto> findByUserDetailType(Byte userType, Long companyId, Long userId, boolean quitJob, boolean disable,
                                               boolean deleted);
}
