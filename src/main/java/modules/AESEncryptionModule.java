package modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESEncryptionModule {

    //Context 생성
    ConfigurableApplicationContext context = new GenericXmlApplicationContext();
    //Environment 생성
    ConfigurableEnvironment environment = context.getEnvironment();
    //PropertySource 다 가져오기
    MutablePropertySources propertySources = environment.getPropertySources();

    private final String AES_ALGORITHM = "AES";
    private final String AES_MODE = "AES/ECB/PKCS5Padding";

    public String encrypt(String data) throws Exception {
        propertySources.addLast(new ResourcePropertySource("classpath:application.properties"));
        String AES_KEY = environment.getProperty("AES_KEY");

        Cipher cipher = Cipher.getInstance(AES_MODE);
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        propertySources.addLast(new ResourcePropertySource("classpath:application.properties"));
        String AES_KEY = environment.getProperty("AES_KEY");

        Cipher cipher = Cipher.getInstance(AES_MODE);
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

}