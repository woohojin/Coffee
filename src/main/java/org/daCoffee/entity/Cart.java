package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {

  @EmbeddedId
  private CartId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("memberId")
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productCode")
  @JoinColumn(name = "product_code")
  private Product product;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "product_grinding", nullable = false, length = 1)
  private String productGrinding;

  public void updateQuantity(int quantity) {
    this.quantity = quantity;
  }
}
