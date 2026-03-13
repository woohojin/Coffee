package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "bean")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bean {

  @Id
  @Column(name = "product_code", length = 10)
  private String productCode;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY) // Bean -> product 단방향이라 Lazy 가능
  @JoinColumn(name = "product_code")
  private Product product;

  @Column(name = "bean_species", length = 20)
  private String beanSpecies;

  @Column(name = "bean_company", length = 20)
  private String beanCompany;

  @Column(name = "bean_use_by_date", length = 50)
  private String beanUseByDate;

  @Column(name = "bean_country", length = 50)
  private String beanCountry;

  @Column(name = "bean_register_name", nullable = false, length = 5)
  private String beanRegisterName;

  @Column(name = "bean_register_date", nullable = false)
  private LocalDate beanRegisterDate;

  @Column(name = "bean_modifier_name", length = 5)
  private String beanModifierName;

  @Column(name = "bean_modifier_date")
  private LocalDate beanModifierDate;
}