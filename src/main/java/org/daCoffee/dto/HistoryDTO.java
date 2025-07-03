package org.daCoffee.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HistoryDTO {
  String orderId;
  int memberTier;
  String memberId;
  String memberName;
  String memberCompanyName;
  String memberFranCode;
  String productCode;
  String productName;
  String productUnit;
  int productPrice;
  int quantity;
  String orderDate;
  String deliveryAddress;
  String detailDeliveryAddress;
  int productGrinding;
  int totalPrice;
  String deliveryCode;
  String historyModifierName;
  String historyModifierDate;
}