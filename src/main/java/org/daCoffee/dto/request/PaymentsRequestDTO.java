package org.daCoffee.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentsRequestDTO {
    private String paymentKey;
    private String orderId;
    private Integer amount;
}
