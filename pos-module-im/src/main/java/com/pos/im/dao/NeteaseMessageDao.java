/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.NeteaseMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 睿智
 * @version 1.0, 2017/9/21
 */
@Repository
public interface NeteaseMessageDao {

    public void addMsg(@Param("msg") NeteaseMessage neteaseMessage);

    public Long findByMsgServerId(@Param("msgServerId") String msgServerId);
}
