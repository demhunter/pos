/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.VersionInstruction;
import com.pos.common.util.mvc.support.LimitHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 版本介绍相关Dao
 *
 * @author wangbing
 * @version 1.0, 2017/12/13
 */
@Repository
public interface VersionInstructionDao {

    /**
     * 根据版本号获取版本介绍
     *
     * @param version   点分三位版本号
     * @param available true：有效；false：无效；null：不限
     * @return 版本介绍
     */
    VersionInstruction findByVersion(
            @Param("version") String version,
            @Param("available") Boolean available);

    /**
     * 根据版本介绍id获取版本介绍
     *
     * @param instructionId 版本介绍id
     * @return 版本介绍
     */
    VersionInstruction findById(@Param("instructionId") Long instructionId);

    /**
     * 保存版本介绍信息
     *
     * @param instruction 介绍信息
     */
    void save(@Param("instruction") VersionInstruction instruction);

    /**
     * 跟新版本介绍信息
     *
     * @param instruction 介绍信息
     */
    void update(@Param("instruction") VersionInstruction instruction);

    /**
     * 启用、禁用版本更新说明
     *
     * @param instructionId 版本介绍id
     * @param available     true：启用；false：禁用
     * @param updateUserId  更新操作人
     */
    void updateAvailable(
            @Param("instructionId") Long instructionId,
            @Param("available") boolean available,
            @Param("updateUserId") Long updateUserId);

    /**
     * 查询版本更新说明数量
     *
     * @param available true：只查询启用状态；false：只查询禁用状态；null：不限
     * @return 版本更新说明数量
     */
    int queryCount(@Param("available") Boolean available);

    /**
     * 查询版本更新说明列表
     *
     * @param available   true：只查询启用状态；false：只查询禁用状态；null：不限
     * @param limitHelper 分页参数
     * @return 版本更新说明列表
     */
    List<VersionInstruction> queryInstructions(
            @Param("available") Boolean available,
            @Param("limitHelper") LimitHelper limitHelper);

}
