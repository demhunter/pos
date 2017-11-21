/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.dto.HotKeyDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 热词推荐相关Dao
 *
 * @author wangbing
 * @version 1.0, 2016/12/29
 */
@Repository
public interface HotKeyDao {

    /**
     * 新增一个热词
     *
     * @param hotKey 热词信息
     */
    void saveHotKey(@Param("hotKey") HotKeyDto hotKey);

    /**
     * 根据热词名称查询热词信息（全匹配查询）
     *
     * @param keyName 热词名称
     * @return 热词信息
     */
    HotKeyDto queryHotKeyByName(@Param("keyName") String keyName);

    /**
     * 批量更新热词信息
     *
     * @param hotKeyDtos
     */
    void updateHotKeys(@Param("hotKeys") List<HotKeyDto> hotKeyDtos);
}
