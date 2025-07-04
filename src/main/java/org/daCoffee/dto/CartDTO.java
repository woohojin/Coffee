package org.daCoffee.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CartDTO {
  String memberId;
  String productCode;
  String productName;
  String productFile;
  String productUnit;
  int productPrice;
  int productSoldOut;
  int quantity;
  int productGrinding;
  int productType;
}
