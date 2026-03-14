package org.daCoffee.handler;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.module.AESEncryptionModule;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Converter
public class EncryptionConverter implements AttributeConverter<String, String> {
  private static final AESEncryptionModule aesEncryptionModule;

  static {
    aesEncryptionModule = new AESEncryptionModule();

    try (InputStream input = EncryptionConverter.class.getClassLoader()
      .getResourceAsStream("config/secrets.properties")) {

      if (input == null) {
        throw new RuntimeException("Unable to find config/secrets.properties");
      }

      Properties props = new Properties();
      props.load(input);

      String aesKey = props.getProperty("SECRET_AES_KEY");
      if (aesKey == null || aesKey.isEmpty()) {
        throw new RuntimeException("SECRET_AES_KEY not found");
      }
      aesEncryptionModule.setAesKey(aesKey);

      String aesIv = props.getProperty("SECRET_AES_IV");
      if (aesIv == null || aesIv.isEmpty()) {
        throw new RuntimeException("SECRET_AES_IV not found");
      }
      aesEncryptionModule.setAesIv(aesIv);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load AES key", e);
    }
  }

  @Override
  public String convertToDatabaseColumn(String attribute) {
    if (attribute == null) return null;
    try {
      return aesEncryptionModule.encrypt(attribute);
    } catch (Exception e) {
      throw new RuntimeException("Encryption failed", e);
    }
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    try {
      return aesEncryptionModule.decrypt(dbData);
    } catch (Exception e) {
      throw new RuntimeException("Decryption failed", e);
    }
  }
}
