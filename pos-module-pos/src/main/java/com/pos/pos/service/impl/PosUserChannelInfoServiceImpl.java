/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service.impl;

import com.pos.common.util.validation.FieldChecker;
import com.pos.pos.dao.PosUserChannelDao;
import com.pos.pos.domain.Twitter;
import com.pos.pos.service.PosUserChannelInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 快捷收款渠道商ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/8/26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosUserChannelInfoServiceImpl implements PosUserChannelInfoService {

    @Resource
    private PosUserChannelDao posUserChannelDao;

    @Override
    public Twitter get(Long channelUserId) {
        FieldChecker.checkEmpty(channelUserId, "channelUserId");

        return posUserChannelDao.get(channelUserId);
    }
}
