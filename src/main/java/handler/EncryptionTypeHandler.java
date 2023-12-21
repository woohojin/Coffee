package handler;

import modules.AESEncryptionModule;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EncryptionTypeHandler extends BaseTypeHandler<String> {

  AESEncryptionModule aesEncryptionModule = new AESEncryptionModule();

  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
    preparedStatement.setString(i, s);
  }

  @Override
  public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
    String value = resultSet.getString(columnName);
    System.out.println("value check " + value + " " + columnName);
    try {
      if(columnName.equals("member_tel")) {
        String encryptValue = aesEncryptionModule.encrypt(value);
        System.out.println("encryptValue = " + encryptValue);
        String decryptValue = aesEncryptionModule.decrypt(encryptValue);
        System.out.println("decryptValue = " + decryptValue);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return value;
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
