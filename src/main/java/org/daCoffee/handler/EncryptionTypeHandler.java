package org.daCoffee.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.MappedTypes;
import org.daCoffee.module.AESEncryptionModule;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
@Alias("encryptionTypeHandler")
@MappedTypes(String.class)
public class EncryptionTypeHandler extends BaseTypeHandler<String> {
  private static final AESEncryptionModule aesEncryptionModule;

  // 키를 직접적으로 가져오는 이유는 Spring에서 관리 시 encryptionTypeHandler가 String 타입 컬럼에 전체 적용된다는 문제가 있어서 직접 인스턴스화 하고 관리하도록 함
  // Application.properties에 있는 AES_KEY는 secrets.properties에서 가져오는데 Spring에서 주입해주는 것이라 Spring에서 관리하지 않는 EncryptionTypeHandler는 AES_KEY를 주입 받을 수 없으므로
  // 직접 secrets.properties에 있는 SECRET_AES_KEY를 가져오게 되었음

  // static을 사용하는 이유는 Mybatis에서 EncryptionTypeHandler를 과도하게 불러오는 현상 때문에 정적으로 만들어 하나의 인스턴스로 모든 컬럼을 암호화 하게 만들기 위함임
  // 하지만 여전히 과도하게 불러오는 현상이 있지만 횟수가 많지 않아 성능에 영향을 주지 않을거라 판단.
  static {
    log.info("Initializing EncryptionTypeHandler static block");
    aesEncryptionModule = new AESEncryptionModule();
    try (InputStream input = EncryptionTypeHandler.class.getClassLoader().getResourceAsStream("config/secrets.properties")) {
      if (input == null) {
        throw new RuntimeException("Unable to find config/secrets.properties in classpath");
      }
      Properties props = new Properties();
      props.load(input);
      String aesKey = props.getProperty("SECRET_AES_KEY");
      if (aesKey == null || aesKey.isEmpty()) {
        throw new RuntimeException("SECRET_AES_KEY not found in secrets.properties");
      }
      aesEncryptionModule.setAesKey(aesKey); // Setter 사용
    } catch (Exception e) {
      throw new RuntimeException("Failed to load SECRET_AES_KEY from secrets.properties", e);
    }
  }

  public EncryptionTypeHandler() {
    log.debug("Default constructor called by MyBatis");
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

    if (value == null) {
      return null;
    }

    try {
      return aesEncryptionModule.decrypt(value);
    } catch (Exception e) {
      throw new SQLException("Decryption failed", e);
    }
  }

  @Override
  public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
    String value = resultSet.getString(columnIndex);

    if (value == null) {
      return null;
    }

    try {
      return aesEncryptionModule.decrypt(value);
    } catch (Exception e) {
      throw new SQLException("Decryption failed", e);
    }
  }

  @Override
  public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
    String value = callableStatement.getString(columnIndex);

    if (value == null) {
      return null;
    }

    try {
      return aesEncryptionModule.decrypt(value);
    } catch (Exception e) {
      throw new SQLException("Decryption failed", e);
    }
  }
}
