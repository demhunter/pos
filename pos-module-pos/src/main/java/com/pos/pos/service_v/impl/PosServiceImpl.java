/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.service_v.impl;

import com.pos.common.sms.service.SmsService;
import com.pos.pos.constants.PosConstants;
import com.pos.pos.dao.AuthorityDao;
import com.pos.pos.dao.PosTwitterDao;
import com.pos.pos.dao.PosUserChannelDao;
import com.pos.pos.dao.PosUserJuniorDao;
import com.pos.pos.service_v.AuthorityService;
import com.pos.pos.service_v.PosService;
import com.pos.pos.service_v.TwitterService;
import com.pos.user.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * POS 相关Service实现类
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PosServiceImpl implements PosService {

    private static final Logger LOG = LoggerFactory.getLogger(PosServiceImpl.class);

    @Resource
    private SmsService smsService;

    @Resource
    private CustomerService customerService;

    @Resource
    private TwitterService twitterService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private PosUserChannelDao posUserChannelDao;

    @Resource
    private PosUserJuniorDao posUserJuniorDao;

    @Resource
    private PosTwitterDao posTwitterDao;

    @Resource
    private PosConstants posConstants;


}
