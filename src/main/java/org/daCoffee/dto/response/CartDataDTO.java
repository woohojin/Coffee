package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Data;
import org.daCoffee.dto.CartDTO;

import java.util.List;

@Data
@Builder
public class CartDataDTO {
  private int cartCount;
  private int sumPrice;
  private int deliveryFee;
  private int totalPrice;
  private List<CartDTO> list;
}