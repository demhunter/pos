/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.enumHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * EnumIntTypeHandler
 *
 * @author cc
 * @version 1.0, 2017/3/24
 */
public class EnumIntTypeHandler <E extends Enum<?> & CommonIntEnum> extends BaseTypeHandler<CommonIntEnum> {

    private Class<E> clazz;

    public EnumIntTypeHandler(Class<E> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        this.clazz = clazz;
    }

    @Override
    public CommonIntEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return codeOf(clazz, callableStatement.getInt(i));
    }

    @Override
    public CommonIntEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return codeOf(clazz, resultSet.getInt(i));
    }

    @Override
    public CommonIntEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return codeOf(clazz, resultSet.getInt(s));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CommonIntEnum commonIntEnum,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, commonIntEnum.getCode());
    }

    private static <E extends Enum<?> & CommonIntEnum> E codeOf(Class<E> clazz, int code) {
        E[] enumConstants = clazz.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getCode() == code) {
                return e;
            }
        }

        return null;
    }
}
