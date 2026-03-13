package org.daCoffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryDTO {
  private String orderId;
  private int memberTier;
  private String memberId;
  private String memberName;
  private String memberCompanyName;
  private String memberFranCode;
  private String productCode;
  private String productName;
  private String productUnit;
  private int productPrice;
  private int quantity;
  private String orderDate;
  private String deliveryAddress;
  private String detailDeliveryAddress;
  private int productGrinding;
  private int totalPrice;
  private String deliveryCode;
  private String historyModifierName;
  private String historyModifierDate;
}