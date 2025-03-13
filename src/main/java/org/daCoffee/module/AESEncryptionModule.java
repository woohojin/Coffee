package org.daCoffee.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESEncryptionModule {
    private final String AES_ALGORITHM = "AES";
    private final String AES_MODE = "AES/ECB/PKCS5Padding";

    private static final Logger LOGGER = LoggerFactory.getLogger(AESEncryptionModule.class);
    private String AES_KEY;

    // mybatis에서 인스턴스화를 진행하기 위해서 기본 생성자가 필요하기에 있음
    public AESEncryptionModule() {
    }

    // EncryptionTypeHandler에서 키를 가져오기 위해 AES_KEY 변수가 private여서 setter를 사용함
    public void setAesKey(String aesKey) {
      if (aesKey == null || aesKey.isEmpty()) {
        throw new IllegalArgumentException("AES_KEY cannot be null or empty");
      }
      this.AES_KEY = aesKey;
    }

    public String encrypt(String data) throws Exception {
      LOGGER.info("Encrypting data: {}", data);

      if (data == null) {
          throw new IllegalArgumentException("Data to encrypt cannot be null");
      }

      Cipher cipher = Cipher.getInstance(AES_MODE);
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

}