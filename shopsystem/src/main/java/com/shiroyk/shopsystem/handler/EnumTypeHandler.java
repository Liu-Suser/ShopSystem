package com.shiroyk.shopsystem.handler;

import com.shiroyk.shopsystem.constant.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, ((BaseEnum) e).getValue());
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.wasNull() ? null : getEnum(resultSet.getInt(s));
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.wasNull() ? null : getEnum(resultSet.getInt(i));
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.wasNull() ? null : getEnum(callableStatement.getInt(i));
    }

    private E getEnum(int value) {
        E[] enums = type.getEnumConstants();
        for (E e : enums) {
            if (((BaseEnum) e).getValue() == value) {
                return e;
            }
        }
        return null;
    }
}
