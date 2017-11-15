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
 * MyBatis枚举类型Handler
 *
 * @author cc
 * @version 1.0, 2017/3/24
 */
public class EnumByteTypeHandler<E extends Enum<?> & CommonByteEnum> extends BaseTypeHandler<CommonByteEnum> {

    private Class<E> clazz;

    public EnumByteTypeHandler(Class<E> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CommonByteEnum commonByteEnum,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, commonByteEnum.getCode());
    }

    @Override
    public CommonByteEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return codeOf(clazz, callableStatement.getInt(i));
    }

    @Override
    public CommonByteEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return codeOf(clazz, resultSet.getInt(i));
    }

    @Override
    public CommonByteEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return codeOf(clazz, resultSet.getInt(s));
    }

    private static <E extends Enum<?> & CommonByteEnum> E codeOf(Class<E> clazz, int code) {
        E[] enumConstants = clazz.getEnumConstants();
        for (E e : enumConstants) {
            if (e.getCode() == code) {
                return e;
            }
        }

        return null;
    }
}
