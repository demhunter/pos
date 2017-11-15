/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.gate;

import com.pos.basic.gate.dto.AppVersionDto;

/**
 * APP版本管理的服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/8/16
 */
public interface AppVersionService {

    /**
     * 查询指定类型的APP版本信息.
     *
     * @param appType APP类型: c = 客户端app, e = 业者端app
     * @return
     */
    AppVersionDto findByAppType(String appType);

}