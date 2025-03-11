package org.daCoffee.handler;

import org.daCoffee.module.AESEncryptionModule;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptionTypeHandler extends BaseTypeHandler<String> {

  private final AESEncryptionModule aesEncryptionModule;

  public EncryptionTypeHandler() {
    this.aesEncryptionModule = new AESEncryptionModule();
  }

  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
    String encryptedValue;

    try {
      encryptedValue = aesEncryptionModule.encrypt(s);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    preparedStatement.setString(i, encryptedValue);
  }

  @Override
  public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
    String value = resultSet.getString(columnName);

    try {
        String decryptedValue = aesEncryptionModule.decrypt(value);
        return decryptedValue;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
      return resultSet.getString(columnIndex);
  }

  @Override
  public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
      return callableStatement.getString(columnIndex);
  }
}
