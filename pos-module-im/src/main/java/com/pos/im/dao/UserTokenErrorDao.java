/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dao;

import com.pos.im.domain.UserTokenError;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Description.
 *
 * @author wayne
 * @version 1.0, 2017/4/20
 */
@Repository
public interface UserTokenErrorDao {

    void save(@Param("ute") UserTokenError userTokenError);

}