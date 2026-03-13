package org.daCoffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDTO {
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
    private String memberFranCode;
    private Integer memberTier;
    private String memberDate;
    private String memberDisableDate;
    private String memberModifierName;
    private String memberModifierDate;
    private String memberWithdrawalDate; // 모델에서만 필요함
}

