/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * MQMessageDao
 *
 * @author cc
 * @version 1.0, 2017/1/17
 */
@Repository
public interface MQMessageDao {

    void saveMessage(@Param("msg") Message message);
}
