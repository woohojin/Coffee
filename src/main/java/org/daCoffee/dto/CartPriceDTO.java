package org.daCoffee.dto;

import lombok.Data;

@Data
public class CartPriceDTO {
  private int sumPrice;
  private int deliveryFee;
  private int totalPrice;
  private int cartCount;
}
