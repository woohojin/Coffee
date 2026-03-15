package org.daCoffee.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHistoryRequestDTO {
  private String orderId;
  private String productCode;
  private String memberFranCode;
  private String memberName;
  private String memberCompanyName;
  private String deliveryAddress;
  private String detailDeliveryAddress;
  private String deliveryCode;
}