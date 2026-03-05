package org.daCoffee.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PaymentsDataDTO {
  private String orderId;
  private String customerKey;
  private String orderName;
  private int totalPrice;
  private MemberDTO member;
  private List<CartDTO> cartItems;
}