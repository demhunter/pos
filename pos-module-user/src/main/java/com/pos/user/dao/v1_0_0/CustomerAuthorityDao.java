/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao.v1_0_0;

import com.pos.user.domain.v1_0_0.CustomerAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * C端用户权限相关DAO
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@Repository
public interface CustomerAuthorityDao {

    void save(@Param("authority") CustomerAuthority authority);

}
