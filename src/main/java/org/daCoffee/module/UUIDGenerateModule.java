package org.daCoffee.module;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class UUIDGenerateModule {

    public String generateOrderId() throws Exception {
        UUID randomUUID = UUID.randomUUID();
        String UUIDWithoutHyphens = randomUUID.toString().replace("-","");
        String UUID8Digits = UUIDWithoutHyphens.substring(0, 8);

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        String orderId = formattedDate + "_" + UUID8Digits;

        return orderId;
    }

    public String generateCustomerKey(String memberId) throws Exception {

        //Context 생성
        ConfigurableApplicationContext context = new GenericXmlApplicationContext();
        //Environment 생성
        ConfigurableEnvironment environment = context.getEnvironment();
        //PropertySource 다 가져오기
        MutablePropertySources propertySources = environment.getPropertySources();

        propertySources.addLast(new ResourcePropertySource("classpath:application.properties"));

        String keyString = environment.getProperty("TOSS_CUSTOMER_KEY");
        String customString = memberId + keyString;

        assert keyString != null;
        UUID customUUID = UUID.nameUUIDFromBytes(customString.getBytes(StandardCharsets.UTF_8));
        String UUIDWithoutHyphens = customUUID.toString().replace("-","");
        String UUID8Digits = UUIDWithoutHyphens.substring(0, 8);

        StringBuilder insertSpecialCharacterUUID = new StringBuilder(UUID8Digits);
        insertSpecialCharacterUUID.insert(UUID8Digits.length() - 5, '_');

        String customerKey = insertSpecialCharacterUUID.toString();

        return customerKey;
    }
}
