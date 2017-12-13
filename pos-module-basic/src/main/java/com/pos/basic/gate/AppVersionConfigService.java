/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.gate;

import com.pos.basic.gate.dto.AppVersionConfigDto;

/**
 * APP版本管理的服务接口.
 *
 * @author wangbing
 * @version 2.0, 2016/12/13
 */
public interface AppVersionConfigService {

    /**
     * 查询指定类型的APP版本配置信息.
     *
     * @return APP版本配置信息.
     */
    AppVersionConfigDto getConfig();

}