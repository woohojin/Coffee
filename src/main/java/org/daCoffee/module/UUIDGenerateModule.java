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
    private String tossCustomerKey;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    public String generateOrderId() {
        String uuid8Digits = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return LocalDate.now().format(DATE_FORMATTER) + "_" + uuid8Digits;
    }

    public String generateCustomerKey(String memberId) {
        String uuid8Digits = UUID.nameUUIDFromBytes((memberId + tossCustomerKey).getBytes(StandardCharsets.UTF_8))
          .toString().replace("-", "").substring(0, 8);

        return new StringBuilder(uuid8Digits).insert(uuid8Digits.length() - 5, '_').toString();
    }
}
