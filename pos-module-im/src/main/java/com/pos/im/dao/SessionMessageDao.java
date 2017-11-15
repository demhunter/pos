/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.im.dto.message.SessionMessageDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM案例会话的消息记录的DAO.
 *
 * @author wayne
 * @version 1.0, 2016/10/25
 */
@Repository
public interface SessionMessageDao {

    void saveBatch(@Param("list") List<SessionMessageDto> messageList);

    /**
     * 统计指定会话的消息数量.
     *
     * @param sessionId   会话ID
     * @return
     */
    int countBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 查询指定会话的消息记录.
     *
     * @param sessionId   会话ID
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数
     * @return
     */
    List<SessionMessageDto> findListBySessionId(@Param("sessionId") Long sessionId,
                                                @Param("limitHelper") LimitHelper limitHelper,
                                                @Param("orderHelper") OrderHelper orderHelper);

}
