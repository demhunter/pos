/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.HistoryMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IM历史消息的处理记录的DAO.
 *
 * @author wayne
 * @version 1.0, 2016/10/24
 */
@Repository
public interface HistoryMessageDao {

    void save(@Param("histMsg") HistoryMessage message);

    void update(@Param("histMsg") HistoryMessage message);

    HistoryMessage get(@Param("id") Long id);

    HistoryMessage findLast();

    HistoryMessage findByHistDate(@Param("histDate") int histDate);

    List<HistoryMessage> findListByStatus(
            @Param("status") byte[] status, @Param("maxSize") int maxSize);

}
