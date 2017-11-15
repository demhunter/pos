/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.SessionOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 与IM会话关联的订单DAO.
 *
 * @author wayne
 * @version 1.0, 2016/12/12
 */
@Deprecated
@Repository
public interface SessionOrderDao {

    void save(@Param("so") SessionOrder sessionOrder);

    SessionOrder find(@Param("sessionId") Long sessionId,
                      @Param("orderContentType") Byte orderContentType);

    Integer delete(@Param("sessionId") Long sessionId,
                   @Param("orderId") Long orderId,
                   @Param("orderContentType") Byte orderContentType);

}
