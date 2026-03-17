package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberProfileDTO {
    private String memberId;
    private String memberName;
    private String memberAddress;
    private String memberDetailAddress;
    private String memberDeliveryAddress;
    private String memberDetailDeliveryAddress;
    private String memberTel;
    private String memberCompanyName;
    private String memberCompanyTel;
    private String memberEmail;
    private String memberFile;
}
