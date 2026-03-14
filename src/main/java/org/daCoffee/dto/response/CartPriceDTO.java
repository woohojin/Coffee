package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartPriceDTO {
  private int sumPrice;
  private int deliveryFee;
  private int totalPrice;
  private int cartCount;
}
