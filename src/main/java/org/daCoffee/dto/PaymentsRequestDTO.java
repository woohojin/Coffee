package org.daCoffee.dto;

import lombok.Data;

@Data
public class PaymentsRequestDTO {
    private String paymentKey;
    private String orderId;
    private String amount;
}
