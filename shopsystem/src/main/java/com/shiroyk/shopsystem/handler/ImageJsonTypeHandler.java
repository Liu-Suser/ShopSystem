package com.shiroyk.shopsystem.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiroyk.shopsystem.entity.Image;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageJsonTypeHandler extends BaseTypeHandler<Image> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Image image, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, new ObjectMapper().writeValueAsString(image));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getJsonpObject(resultSet.getString(s));
    }

    @Override
    public Image getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getJsonpObject(resultSet.getString(i));
    }
    
    @Override
    public Image getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getJsonpObject(callableStatement.getString(i));
    }

    private Image getJsonpObject(String str) {
        if (null != str) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(str, Image.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return Image.empty();
    }
}
