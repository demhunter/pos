/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.im.condition.query.InternalMessageCondition;
import com.pos.im.domain.InternalMessage;
import com.pos.im.domain.InternalMessageReceiver;
import com.pos.im.dto.message.InternalMessageDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM站内消息DAO.
 *
 * @author wayne
 * @version 1.0, 2016/11/4
 */
@Repository
public interface InternalMessageDao {

    void save(@Param("msg") InternalMessage message);

    void saveReceivers(@Param("list") List<InternalMessageReceiver> receivers);

    void saveReceiver(@Param("receiver") InternalMessageReceiver receiver);

    InternalMessageDto get(@Param("id") Long id);

    /**
     * 获取指定条件的站内消息数量.
     *
     * @param condition 查询条件
     * @return
     */
    int getTotalByCondition(@Param("condition") InternalMessageCondition condition);

    /**
     * 查询指定条件的站内消息列表.
     *
     * @param condition 查询条件
     * @param limitHeler 分页参数
     * @param orderHelper 排序参数
     * @return
     */
    List<InternalMessageDto> findByCondition(@Param("condition") InternalMessageCondition condition,
                                             @Param("limitHeler") LimitHelper limitHeler,
                                             @Param("orderHelper") OrderHelper orderHelper);

}