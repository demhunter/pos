/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.condition.query.CompanyEmployeesCondition;
import com.pos.user.domain.Employee;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.employee.EmployeeInfoDto;
import com.pos.user.dto.SessionUserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * B端从业者相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
@Repository
public interface EmployeeDao {

    void save(@Param("e") Employee employee);

    void update(@Param("e") Employee employee);

    Employee getByUserId(@Param("userId") Long userId);

    EmployeeDto findEmployeeByUserId(
            @Param("userId") Long userId,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    EmployeeDto findEmployeeByUserName(
            @Param("userName") String userName,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    EmployeeDto findEmployeeByUserPhone(
            @Param("userPhone") String userPhone,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    EmployeeDto findByInvitationCode(
            @Param("invitationCode") String invitationCode,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    /**
     * 根据用户姓名查询业者列表.
     *
     * @param name      姓名(支持首字匹配)
     * @param companyId 所属公司ID(可空)
     * @param maxSize   最大查询条数
     * @param available true表示只返回可用账号，false表示只返回禁用账号，null表示返回所有账号
     * @param deleted   true表示只返回被删除账号，false表示只返回正常的账号，null表示返回所有账号
     */
    List<EmployeeDto> findEmployeesByName(@Param("name") String name,
                                          @Param("companyId") Long companyId,
                                          @Param("maxSize") int maxSize,
                                          @Param("deleted") Boolean deleted,
                                          @Param("available") Boolean available,
                                          @Param("quitJob") Boolean quitJob);

    /**
     * 查找员工，用于查找IM会话成员
     * @param name
     * @param userPhone
     * @param company
     * @param deleted
     * @param available
     * @return
     */
    List<SessionUserDto> findEmployeeForImMember(@Param("name") String name,
                                                 @Param("userPhone") String userPhone,
                                                 @Param("company") String company,
                                                 @Param("deleted") Boolean deleted,
                                                 @Param("available") Boolean available,
                                                 @Param("quitJob") Boolean quitJob);

    /**
     * 根据用户姓名和类型查询业者信息
     *
     * @param name 推客姓名
     * @param employeeType 业者类型
     * @param deleted 是否返回被删除的账号
     * @param available true表示只返回可用账号，false表示只返回禁用账号，null表示返回所有账号
     * @return 业者信息
     */
    List<EmployeeDto> findEmployeesByNameAndType(@Param("name") String name,
                                          @Param("employeeType") Byte employeeType,
                                          @Param("deleted") Boolean deleted,
                                          @Param("available") Boolean available);

    /**
     * 根据公司ID查询业者信息.
     *
     * @param companiesId 所属公司ID
     * @param maxSize     最大查询条数
     * @param available   true表示只返回可用账号，false表示只返回禁用账号，null表示返回所有账号
     * @param deleted     true表示只返回被删除账号，false表示只返回正常的账号，null表示返回所有账号
     */
    List<EmployeeDto> findEmployeesByCompaniesId(@Param("companiesId") Long[] companiesId,
                                                 @Param("maxSize") int maxSize,
                                                 @Param("deleted") Boolean deleted,
                                                 @Param("available") Boolean available,
                                                 @Param("quitJob") Boolean quitJob);

    /**
     * 查询公司业者列表
     *
     * @param condition   查询条件 {@link CompanyEmployeesCondition}
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return 查询结果
     */
    List<EmployeeDto> getEmployees(
            @Param("condition") CompanyEmployeesCondition condition,
            @Param("limitHelper") LimitHelper limitHelper,
            @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 查询雇员数
     *
     * @param condition 查询条件 {@link CompanyEmployeesCondition}
     * @return 查询结果
     */
    int getTotalEmployeeCount(@Param("condition") CompanyEmployeesCondition condition);

    /**
     * 查询用户的业者收藏列表信息
     *
     * @param userId 用户ID
     * @param userType 用户类型
     * @param limitHelper 分页参数
     * @return 业者收藏列表信息
     */
    List<EmployeeDto> queryCollectionEmployees(
            @Param("userId") Long userId,
            @Param("userType") String userType,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询一组指定的业者信息
     *
     * @param userIds 用户ID
     * @param deleted 是否返回被删除的账号
     * @param available 是否返回被禁用的账号
     * @return 业者信息列表
     */
    List<EmployeeDto> findInUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("deleted") Boolean deleted,
            @Param("available") Boolean available);

    /**
     * 查询业者列表
     *
     * @param deleted     是否返回被删除的账号
     * @param available   是否返回被禁用的账号
     * @param limitHelper 分页参数
     * @return 业者列表
     */
    List<EmployeeDto> queryEmployees(
            @Param("deleted") Boolean deleted,
            @Param("available") Boolean available,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询业者列表
     *
     * @param userDetailType
     * @param companyId
     * @param quitJob
     * @param available
     * @param deleted
     * @return
     */
    List<EmployeeInfoDto> findByUserDetailType(
            @Param("userDetailType") Byte userDetailType,
            @Param("companyId") Long companyId,
            @Param("quitJob") Boolean quitJob,
            @Param("available") Boolean available,
            @Param("deleted") Boolean deleted);

    /**
     * 查询业者所在公司
     *
     * @param company
     * @return
     */
    String findCompany(@Param("companyId") Long company);
}