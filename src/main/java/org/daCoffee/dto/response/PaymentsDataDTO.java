package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.daCoffee.entity.Cart;
import org.daCoffee.entity.Member;

import java.util.List;

@Getter
@Builder
public class PaymentsDataDTO {
  private String orderId;
  private String customerKey;
  private String orderName;
  private int totalPrice;
  private Member member;
  private List<Cart> cartItems;
}