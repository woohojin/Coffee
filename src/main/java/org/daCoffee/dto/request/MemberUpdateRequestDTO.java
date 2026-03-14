package org.daCoffee.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequestDTO {
  private String memberName;
  private String memberPassword;
  private String memberTel;
  private String memberCompanyTel;
  private String memberAddress;
  private String memberDetailAddress;
  private String memberDeliveryAddress;
  private String memberDetailDeliveryAddress;
  private String memberEmail;
  private String memberFile;
}