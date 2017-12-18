/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.gate.impl;

import com.pos.basic.gate.dto.AppVersionConfigDto;
import com.pos.basic.dao.AppVersionConfigDao;
import com.pos.basic.domain.AppVersionConfig;
import com.pos.basic.gate.AppVersionConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * APP版本管理的实现类.
 *
 * @author wangbing
 * @version 2.0, 2016/12/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppVersionConfigServiceImpl implements AppVersionConfigService {

    @Resource
    private AppVersionConfigDao appVersionConfigDao;

    @Override
    public AppVersionConfigDto getConfig() {
        return convert2AppVersionDto(appVersionConfigDao.getConfig());
    }

    private AppVersionConfigDto convert2AppVersionDto(AppVersionConfig appVersionConfig) {
        if (appVersionConfig != null) {
            AppVersionConfigDto dto = new AppVersionConfigDto();
            dto.setId(appVersionConfig.getId());
            dto.setCurrentVersion(appVersionConfig.getCurrentVersion());
            dto.setMinVersion(appVersionConfig.getMinVersion());
            dto.setCurrentUrl(appVersionConfig.getCurrentUrl());
            dto.setLatestUrl(appVersionConfig.getLatestUrl());
            dto.setLatestAndroidUrl(appVersionConfig.getLatestAndroidUrl());
            dto.setLatestAndroidMd5(appVersionConfig.getLatestAndroidMd5());
            return dto;
        } else {
            return null;
        }
    }

}