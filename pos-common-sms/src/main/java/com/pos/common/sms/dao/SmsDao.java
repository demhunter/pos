/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.sms.dao;

import com.pos.common.sms.domain.Sms;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 短信相关DAO
 *
 * Created by cc on 16/6/13.
 */
@Repository
public interface SmsDao {

    /**
     * 记录短信发送信息
     *
     * @param sms 短信领域对象
     */
    void saveSmsInfo(@Param("sms") Sms sms);
}
