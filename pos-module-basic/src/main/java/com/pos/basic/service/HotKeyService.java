/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service;

import com.pos.basic.dto.HotKeyDto;
import com.pos.common.util.mvc.support.ApiResult;

import java.util.List;

/**
 * 热词相关Service
 *
 * @author wangbing
 * @version 1.0, 2017/01/06
 */
public interface HotKeyService {

    /**
     * 更新热词信息
     *
     * @param hotKeyDto
     * @return true = 更新成功，false = 更新失败
     */
    ApiResult<HotKeyDto> saveHotKey(HotKeyDto hotKeyDto);

    /**
     * 批量更新热词信息
     *
     * @param hotKeyDtoList 需要更新的列表
     * @return true = 更新成功，false = 更新失败
     */
    boolean updateHotkeys(List<HotKeyDto> hotKeyDtoList);

    /**
     * 根据查询关键字查询数据库中的热词信息
     *
     * @param queryKey 关键字
     * @return 热词信息
     */
    ApiResult<HotKeyDto> queryHotKeyByName(String queryKey);

    /**
     * 处理热词信息
     *
     * @param hotKey 关键字
     */
    void handleHotKey(String hotKey);
}
