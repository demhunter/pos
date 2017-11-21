/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.gate.impl;

import com.pos.basic.gate.dto.AppVersionDto;
import com.pos.basic.dao.AppVersionDao;
import com.pos.basic.domain.AppVersion;
import com.pos.basic.gate.AppVersionService;
import com.pos.common.util.validation.FieldChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * APP版本管理的实现类.
 *
 * @author wayne
 * @version 1.0, 2016/8/16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppVersionServiceImpl implements AppVersionService {

    @Resource
    private AppVersionDao appVersionDao;

    @Override
    public AppVersionDto findByAppType(String appType) {
        FieldChecker.checkEmpty(appType, "appType");
        return convert2AppVersionDto(appVersionDao.findByAppType(appType));
    }

    private AppVersionDto convert2AppVersionDto(AppVersion appVersion) {
        if (appVersion != null) {
            AppVersionDto dto = new AppVersionDto();
            dto.setId(appVersion.getId());
            dto.setAppType(appVersion.getAppType());
            dto.setCurVersion(appVersion.getCurVersion());
            dto.setMinVersion(appVersion.getMinVersion());
            dto.setCurUrl(appVersion.getCurUrl());
            dto.setCurImKey(appVersion.getCurImKey());
            dto.setLatestUrl(appVersion.getLatestUrl());
            dto.setLatestImKey(appVersion.getLatestImKey());
            dto.setLatestAndroidUrl(appVersion.getLatestAndroidUrl());
            dto.setLatestAndroidMd5(appVersion.getLatestAndroidMd5());
            dto.setWebUrl(appVersion.getWebUrl());
            dto.setPrewebUrl(appVersion.getPrewebUrl());
            return dto;
        } else {
            return null;
        }
    }

}