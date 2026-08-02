package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import org.daCoffee.handler.EncryptionConverter;
import java.time.LocalDate;

@Entity
@Table(name = "member_withdrawal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberWithdrawal {

  @Id
  @Column(name = "member_id", length = 20)
  private String memberId;

  @Column(name = "member_company_name", length = 32)
  private String memberCompanyName;

  @Column(name = "member_tel", nullable = false)
  private String memberTel;

  @Column(name = "member_company_tel", length = 32)
  private String memberCompanyTel;

  @Convert(converter = EncryptionConverter.class)
  @Column(name = "member_email", nullable = false)
  private String memberEmail;

  @Column(name = "member_withdrawal_memo", length = 255)
  private String withdrawalMemo;

  @Column(name = "member_withdrawal_date", nullable = false)
  private LocalDate memberWithdrawalDate;
}
