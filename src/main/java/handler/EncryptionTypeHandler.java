package handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptionTypeHandler extends BaseTypeHandler<String> {

  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
    preparedStatement.setString(i, s);
  }

  @Override
  public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
    String value = resultSet.getString(columnName);
    System.out.println("value check " + value + " " + columnName);
    return value;
  }

  @Override
  public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
    String value = resultSet.getString(columnIndex);
    System.out.println("columnIndex " + value + " " + columnIndex);
    return value;
  }

  @Override
  public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
    String value = callableStatement.getString(columnIndex);
    return value;
  }
}
