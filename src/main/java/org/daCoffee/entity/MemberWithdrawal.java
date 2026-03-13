package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
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

  @Column(name = "member_withdrawal_date", nullable = false)
  private LocalDate memberWithdrawalDate;
}
