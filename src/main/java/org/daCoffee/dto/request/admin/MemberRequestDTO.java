package org.daCoffee.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDTO {
  private String memberId;
  private String memberName;
  private String memberCompanyName;
  private String memberTel;
  private String memberCompanyTel;
  private String memberAddress;
  private String memberDetailAddress;
  private String memberDeliveryAddress;
  private String memberDetailDeliveryAddress;
  private String memberEmail;
  private String memberFranCode;
  private int memberTier;
}