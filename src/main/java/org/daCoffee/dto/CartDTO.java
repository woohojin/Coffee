package org.daCoffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartDTO {
  private String memberId;
  private String productCode;
  private String productName;
  private String productFile;
  private String productUnit;
  private int productPrice;
  private boolean productSoldOut;
  private int quantity;
  private int productGrinding;
  private int productType;
}
