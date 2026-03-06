package org.daCoffee.module;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class AESEncryptionModule {
  private static final String AES_ALGORITHM = "AES";
  private static final String AES_MODE = "AES/CBC/PKCS5Padding";

  private String AES_KEY;
  private String AES_IV;

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

  public void setAesIv(String aesIv) {
    if (aesIv == null || aesIv.isEmpty()) {
      throw new IllegalArgumentException("AES_IV cannot be null or empty");
    }
    this.AES_IV = aesIv;
  }

  public String encrypt(String data) throws Exception {
    if (data == null) {
        throw new IllegalArgumentException("Data to encrypt cannot be null");
    }

    Cipher cipher = Cipher.getInstance(AES_MODE);
    SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(AES_IV.getBytes());
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] encryptedBytes = cipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  public String decrypt(String encryptedData) throws Exception {
    Cipher cipher = Cipher.getInstance(AES_MODE);
    SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(AES_IV.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
    byte[] decryptedBytes = cipher.doFinal(decodedBytes);
    return new String(decryptedBytes);
  }
}