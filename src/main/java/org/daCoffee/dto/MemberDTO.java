package org.daCoffee.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberDTO {
    String memberId;
    String memberName;
    String memberCompanyName;
    String memberPassword;
    String memberTel;
    String memberCompanyTel;
    String memberAddress;
    String memberDetailAddress;
    String memberDeliveryAddress;
    String memberDetailDeliveryAddress;
    String memberEmail;
    String memberFile;
    String memberFranCode;
    Integer memberTier;
    String memberDate;
    String memberDisableDate;
    String memberModifierName;
    String memberModifierDate;
    String memberWithdrawalDate; // 모델에서만 필요함
}

