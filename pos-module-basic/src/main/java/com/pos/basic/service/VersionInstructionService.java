/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.version.VersionDto;
import com.pos.basic.dto.version.VersionInstructionDto;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;

/**
 * 版本更新介绍Service
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
public interface VersionInstructionService {

    /**
     * 新增或更新版本更新说明
     *
     * @param instruction 介绍信息
     * @param operator    操作人标识
     * @return 操作结果
     */
    ApiResult<NullObject> saveOrUpdateInstruction(VersionInstructionDto instruction, UserIdentifier operator);

    /**
     * 查询指定的版本更新说明
     *
     * @param instructionId 版本介绍id
     * @return 查询解雇破
     */
    VersionInstructionDto findInstruction(Long instructionId);

    /**
     * 获取指定版本的版本更新说明
     *
     * @param version 版本信息
     * @return 版本更新说明
     */
    VersionInstructionDto findInstruction(VersionDto version);

    /**
     * 启用、禁用版本更新说明
     *
     * @param instructionId 版本介绍id
     * @param available     true：启用；false：禁用
     * @param operator      操作人标识
     * @return 操作结果
     */
    ApiResult<NullObject> updateAvailable(Long instructionId, boolean available, UserIdentifier operator);

    /**
     * 查询版本更新说明列表
     *
     * @param available   true：只查询启用状态；false：只查询禁用状态；null：不限
     * @param limitHelper 分页参数
     * @return 版本更新说明列表
     */
    ApiResult<Pagination<VersionInstructionDto>> queryInstructions(Boolean available, LimitHelper limitHelper);
}
