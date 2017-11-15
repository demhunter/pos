/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mybatis;

/**
 * SQL方言接口
 *
 * @author chichen  Date: 16-4-20 Time: 下午3:57
 */
public interface Dialect {

    String getLimitString(String sql, int offset, int limit);
}
