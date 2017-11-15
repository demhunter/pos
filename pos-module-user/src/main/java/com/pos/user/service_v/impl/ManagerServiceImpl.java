/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service_v.impl;

import com.pos.user.service_v.ManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 平台管理员相关服务接口的实现类.
 *
 * @author wayne
 * @version 1.0, 2016/8/4
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManagerServiceImpl implements ManagerService {

}