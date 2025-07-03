package org.daCoffee.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentsRequestDTO {
    private String paymentKey;
    private String orderId;
    private String amount;
}
