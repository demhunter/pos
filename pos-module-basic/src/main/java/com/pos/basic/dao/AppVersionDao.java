/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dao;

import com.pos.basic.domain.AppVersion;
import org.springframework.stereotype.Repository;

/**
 * APP版本管理DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/16
 */
@Repository
public interface AppVersionDao {

    AppVersion findByAppType(String appType);

}
