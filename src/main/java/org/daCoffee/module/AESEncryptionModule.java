package org.daCoffee.module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESEncryptionModule {

    private final String AES_ALGORITHM = "AES";
    private final String AES_MODE = "AES/ECB/PKCS5Padding";

    @Value("${AES_KEY}")
    private String AES_KEY;

    @PostConstruct
    public void init() {
        System.out.println("AES_KEY from @Value: " + AES_KEY);
        if (AES_KEY == null) {
            throw new IllegalStateException("AES_KEY is null on initialization");
        }
    }

    public String encrypt(String data) throws Exception {
        System.out.println("Encrypting data: " + data);
        if (data == null) {
            throw new IllegalArgumentException("Data to encrypt cannot be null");
        }
        if (AES_KEY == null) {
            throw new IllegalStateException("AES_KEY is null during encryption");
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