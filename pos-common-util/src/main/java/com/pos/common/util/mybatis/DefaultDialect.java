/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mybatis;

/**
 * Dialect默认实现
 *
 * @author chichen  Date: 16-4-20 Time: 下午3:59
 */
public class DefaultDialect implements Dialect {

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        StringBuilder buffer = new StringBuilder(sql.length() + 20);
        buffer.append(this.trim(sql));
        buffer.append(" limit ");
        buffer.append(limit);
        if(offset > 0) {
            buffer.append(" offset ").append(offset);
        }

        buffer.append(";");
        return buffer.toString();
    }

    private String trim(String sql) {
        return sql.endsWith(";") ? sql.substring(0, sql.length() - 1 - ";".length()) : sql;
    }
}
