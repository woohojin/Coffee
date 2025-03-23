package org.daCoffee.module;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class UUIDGenerateModule {

    @Value("${SECRET_TOSS_CUSTOMER_KEY}")
    private String TOSS_CUSTOMER_KEY;

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
        String customString = memberId + TOSS_CUSTOMER_KEY;

        assert TOSS_CUSTOMER_KEY != null;
        UUID customUUID = UUID.nameUUIDFromBytes(customString.getBytes(StandardCharsets.UTF_8));
        String UUIDWithoutHyphens = customUUID.toString().replace("-","");
        String UUID8Digits = UUIDWithoutHyphens.substring(0, 8);

        StringBuilder insertSpecialCharacterUUID = new StringBuilder(UUID8Digits);
        insertSpecialCharacterUUID.insert(UUID8Digits.length() - 5, '_');

        String customerKey = insertSpecialCharacterUUID.toString();

        return customerKey;
    }
}
