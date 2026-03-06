package org.daCoffee.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityUtil {
  private static final char[] CHAR_SET = new char[] {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
  };

  public static class SHA256 {
    public static String encrypt(String text) throws NoSuchAlgorithmException {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(text.getBytes());
      return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] bytes) {
      StringBuilder builder = new StringBuilder();
      for (byte b : bytes) {
        builder.append(String.format("%02x", b));
      }
      return builder.toString();
    }
  }

  public static String getRandomPassword(int size) { //난수 생성기
    if (size <= 0) {
      throw new IllegalArgumentException("Invalid size or character set");
    }

    StringBuilder sb = new StringBuilder();
    SecureRandom sr = new SecureRandom();

    int len = CHAR_SET.length;
    for (int i = 0; i < size; i++) {
      int idx = sr.nextInt(len);
      sb.append(CHAR_SET[idx]);
    }

    return sb.toString();
  }
}
