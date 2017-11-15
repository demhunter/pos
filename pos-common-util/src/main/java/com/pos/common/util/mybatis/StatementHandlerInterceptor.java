/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mybatis;

import com.pos.common.util.basic.ReflectUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Properties;

/**
 * MyBatis使用RowBounds分页时对SQL语句进行拦截重装
 *
 * @author chichen  Date: 16-4-20 Time: 下午3:22
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class})})
public class StatementHandlerInterceptor implements Interceptor {

    private Dialect dialect;

    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if(target instanceof RoutingStatementHandler) {
            target = ReflectUtils.getFieldValue(target, "delegate");
        }

        RowBounds rowBounds = (RowBounds) ReflectUtils.getFieldValue(target, "rowBounds");
        if(rowBounds.getLimit() > 0 && rowBounds.getLimit() < Integer.MAX_VALUE) {
            BoundSql boundSql = ((StatementHandler) target).getBoundSql();
            String sql = boundSql.getSql();
            sql = this.dialect.getLimitString(sql, rowBounds.getOffset(), rowBounds.getLimit());
            ReflectUtils.setFieldValue(boundSql, "sql", sql);
        }

        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        if(dialectClass != null && !dialectClass.isEmpty()) {
            try {
                this.dialect = (Dialect) Class.forName(dialectClass).newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid dialect class " + dialectClass, e);
            }
        } else {
            this.dialect = new DefaultDialect();
        }
    }
}
