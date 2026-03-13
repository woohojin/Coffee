package org.daCoffee.dto.response;

import lombok.Getter;

@Getter
public class CartPriceDTO {
  private int sumPrice;
  private int deliveryFee;
  private int totalPrice;
  private int cartCount;
}
