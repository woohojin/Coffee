package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "mix")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Mix {

  @Id
  @Column(name = "product_code", length = 10)
  private String productCode;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_code")
  private Product product;

  @Column(name = "mix_company", length = 20)
  private String mixCompany;

  @Column(name = "mix_use_by_date", length = 50)
  private String mixUseByDate;

  @Column(name = "mix_register_name", nullable = false, length = 5)
  private String mixRegisterName;

  @Column(name = "mix_register_date", nullable = false)
  private LocalDate mixRegisterDate;

  @Column(name = "mix_modifier_name", length = 5)
  private String mixModifierName;

  @Column(name = "mix_modifier_date")
  private LocalDate mixModifierDate;
}