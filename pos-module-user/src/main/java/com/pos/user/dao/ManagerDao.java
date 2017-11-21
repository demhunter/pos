/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.condition.query.ManagerListCondition;
import com.pos.user.domain.Manager;
import com.pos.user.dto.manager.ManagerDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * B端用户相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
@Repository
public interface ManagerDao {

    void save(@Param("m") Manager manager);

    void update(@Param("m") Manager manager);

    Manager getByUserId(@Param("userId") Long userId);

    ManagerDto findManagerByUserId(
            @Param("userId") Long userId,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    ManagerDto findManagerByUserName(
            @Param("userName") String userName,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    ManagerDto findManagerByUserPhone(
            @Param("userPhone") String userPhone,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    /**
     * 统计指定条件的平台管理员总数.
     *
     * @param condition 公共查询条件
     */
    int getTotal(@Param("condition") ManagerListCondition condition);

    /**
     * 查询指定条件的平台管理员列表.
     *
     * @param condition   公共查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     */
    List<ManagerDto> findManagers(@Param("condition") ManagerListCondition condition,
                                  @Param("limitHelper") LimitHelper limitHelper,
                                  @Param("orderHelper") OrderHelper orderHelper);

    /**
     * 查询平台管理员列表
     *
     * @param deleted     是否返回被删除的账号
     * @param available   是否返回被禁用的账号
     * @param limitHelper 分页参数
     * @return 管理员列表
     */
    List<ManagerDto> queryManagers(
            @Param("deleted") Boolean deleted,
            @Param("available") Boolean available,
            @Param("limitHelper") LimitHelper limitHelper);

}