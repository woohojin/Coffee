package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import org.daCoffee.handler.EncryptionConverter;

import java.time.LocalDate;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 접근 방지( JPA의 상속 접근 허용을 위해 Protected)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부 접근 방지
public class Member {

  @Id
  @Column(name = "member_id", length = 20)
  private String memberId;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_name", nullable = false)
  private String memberName;

  @Column(name = "member_company_name", length = 32)
  private String memberCompanyName;

  @Column(name = "member_password", nullable = false)
  private String memberPassword;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_tel", nullable = false)
  private String memberTel;

  @Column(name = "member_company_tel", length = 32)
  private String memberCompanyTel;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_address", nullable = false)
  private String memberAddress;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_detail_address", nullable = false)
  private String memberDetailAddress;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_delivery_address", nullable = false)
  private String memberDeliveryAddress;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_detail_delivery_address", nullable = false)
  private String memberDetailDeliveryAddress;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_email", nullable = false)
  private String memberEmail;

  @Column(name = "member_file", length = 32)
  private String memberFile;

  @Column(name = "member_fran_code", length = 12)
  private String memberFranCode;

  @Column(name = "member_tier", nullable = false)
  private Integer memberTier;

  @Column(name = "member_disabled_status", nullable = false)
  private boolean memberDisabledStatus;

  @Column(name = "member_date", nullable = false)
  private LocalDate memberDate;

  @Column(name = "member_disable_date")
  private LocalDate memberDisableDate;

  @Column(name = "member_modifier_name", length = 5)
  private String memberModifierName;

  @Column(name = "member_modifier_date")
  private LocalDate memberModifierDate;

  // 회원 정보 수정 (비밀번호 포함)
  public void update(String memberName, String memberPassword, String memberTel,
                     String memberCompanyTel, String memberAddress, String memberDetailAddress,
                     String memberDeliveryAddress, String memberDetailDeliveryAddress,
                     String memberEmail, String memberFile) {
    this.memberName = memberName;
    this.memberPassword = memberPassword;
    this.memberTel = memberTel;
    this.memberCompanyTel = memberCompanyTel;
    this.memberAddress = memberAddress;
    this.memberDetailAddress = memberDetailAddress;
    this.memberDeliveryAddress = memberDeliveryAddress;
    this.memberDetailDeliveryAddress = memberDetailDeliveryAddress;
    this.memberEmail = memberEmail;
    this.memberFile = memberFile;
    this.memberModifierDate = LocalDate.now();
  }

  // 회원 정보 수정 (비밀번호 제외)
  public void updateWithoutPassword(String memberName, String memberTel,
                                    String memberCompanyTel, String memberAddress, String memberDetailAddress,
                                    String memberDeliveryAddress, String memberDetailDeliveryAddress,
                                    String memberEmail, String memberFile) {
    this.memberName = memberName;
    this.memberTel = memberTel;
    this.memberCompanyTel = memberCompanyTel;
    this.memberAddress = memberAddress;
    this.memberDetailAddress = memberDetailAddress;
    this.memberDeliveryAddress = memberDeliveryAddress;
    this.memberDetailDeliveryAddress = memberDetailDeliveryAddress;
    this.memberEmail = memberEmail;
    this.memberFile = memberFile;
    this.memberModifierDate = LocalDate.now();
  }
}
