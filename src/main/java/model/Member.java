package model;

public class Member {
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
    int memberTier;
    String member_date;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCompanyName() {
        return memberCompanyName;
    }

    public void setMemberCompanyName(String memberCompanyName) {
        this.memberCompanyName = memberCompanyName;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }

    public String getMemberCompanyTel() {
        return memberCompanyTel;
    }

    public void setMemberCompanyTel(String memberCompanyTel) {
        this.memberCompanyTel = memberCompanyTel;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberDetailAddress() {
        return memberDetailAddress;
    }

    public void setMemberDetailAddress(String memberDetailAddress) {
        this.memberDetailAddress = memberDetailAddress;
    }

    public String getMemberDeliveryAddress() {
        return memberDeliveryAddress;
    }

    public void setMemberDeliveryAddress(String memberDeliveryAddress) {
        this.memberDeliveryAddress = memberDeliveryAddress;
    }

    public String getMemberDetailDeliveryAddress() {
        return memberDetailDeliveryAddress;
    }

    public void setMemberDetailDeliveryAddress(String memberDetailDeliveryAddress) {
        this.memberDetailDeliveryAddress = memberDetailDeliveryAddress;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberFile() {
        return memberFile;
    }

    public void setMemberFile(String memberFile) {
        this.memberFile = memberFile;
    }

    public String getMemberFranCode() {
        return memberFranCode;
    }

    public void setMemberFranCode(String memberFranCode) {
        this.memberFranCode = memberFranCode;
    }

    public int getMemberTier() {
        return memberTier;
    }

    public void setMemberTier(int memberTier) {
        this.memberTier = memberTier;
    }

    public String getMember_date() {
        return member_date;
    }

    public void setMember_date(String member_date) {
        this.member_date = member_date;
    }

    @Override
    public String toString() {
        return "Member{" +
          "memberId='" + memberId + '\'' +
          ", memberName='" + memberName + '\'' +
          ", memberCompanyName='" + memberCompanyName + '\'' +
          ", memberPassword='" + memberPassword + '\'' +
          ", memberTel='" + memberTel + '\'' +
          ", memberCompanyTel='" + memberCompanyTel + '\'' +
          ", memberAddress='" + memberAddress + '\'' +
          ", memberDetailAddress='" + memberDetailAddress + '\'' +
          ", memberDeliveryAddress='" + memberDeliveryAddress + '\'' +
          ", memberDetailDeliveryAddress='" + memberDetailDeliveryAddress + '\'' +
          ", memberEmail='" + memberEmail + '\'' +
          ", memberFile='" + memberFile + '\'' +
          ", memberFranCode='" + memberFranCode + '\'' +
          ", memberTier=" + memberTier +
          ", member_date='" + member_date + '\'' +
          '}';
    }
}

