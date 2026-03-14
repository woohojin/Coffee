package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.daCoffee.dto.MemberDTO;
import org.daCoffee.entity.Cart;

import java.util.List;

@Getter
@Builder
public class PaymentsDataDTO {
  private String orderId;
  private String customerKey;
  private String orderName;
  private int totalPrice;
  private MemberDTO member;
  private List<Cart> cartItems;
}