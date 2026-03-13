package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
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

  @Column(name = "member_name", nullable = false)
  private String memberName;

  @Column(name = "member_company_name", length = 32)
  private String memberCompanyName;

  @Column(name = "member_password", nullable = false)
  private String memberPassword;

  @Column(name = "member_tel", nullable = false)
  private String memberTel;

  @Column(name = "member_company_tel", length = 32)
  private String memberCompanyTel;

  @Column(name = "member_address", nullable = false)
  private String memberAddress;

  @Column(name = "member_detail_address", nullable = false)
  private String memberDetailAddress;

  @Column(name = "member_delivery_address", nullable = false)
  private String memberDeliveryAddress;

  @Column(name = "member_detail_delivery_address", nullable = false)
  private String memberDetailDeliveryAddress;

  @Column(name = "member_email", nullable = false)
  private String memberEmail;

  @Column(name = "member_file", length = 32)
  private String memberFile;

  @Column(name = "member_fran_code", length = 12)
  private String memberFranCode;

  @Column(name = "member_tier", nullable = false, length = 1)
  private String memberTier;

  @Column(name = "member_disabled_status", nullable = false, length = 1)
  private String memberDisabledStatus;

  @Column(name = "member_date", nullable = false)
  private LocalDate memberDate;

  @Column(name = "member_disable_date")
  private LocalDate memberDisableDate;

  @Column(name = "member_modifier_name", length = 5)
  private String memberModifierName;

  @Column(name = "member_modifier_date")
  private LocalDate memberModifierDate;
}
