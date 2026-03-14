package org.daCoffee.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignUpRequestDTO {
  private String memberId;
  private String memberName;
  private String memberCompanyName;
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
