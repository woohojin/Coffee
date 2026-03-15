package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_num")
  private Integer orderNum;

  @Column(name = "order_id", nullable = false, length = 15)
  private String orderId;

  @Column(name = "member_tier", nullable = false)
  private Integer memberTier;

  @Column(name = "member_id", nullable = false, length = 20)
  private String memberId;

  @Column(name = "member_name", nullable = false)
  private String memberName;

  @Column(name = "member_company_name", length = 32)
  private String memberCompanyName;

  @Column(name = "member_fran_code", length = 12)
  private String memberFranCode;

  @Column(name = "product_code", nullable = false, length = 10)
  private String productCode;

  @Column(name = "product_name", nullable = false, length = 50)
  private String productName;

  @Column(name = "product_unit", nullable = false, length = 20)
  private String productUnit;

  @Column(name = "product_price", nullable = false)
  private Integer productPrice;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "order_date", nullable = false)
  private LocalDateTime orderDate;

  @Column(name = "delivery_address", nullable = false)
  private String deliveryAddress;

  @Column(name = "detail_delivery_address", nullable = false)
  private String detailDeliveryAddress;

  @Column(name = "total_price", nullable = false)
  private Integer totalPrice;

  @Column(name = "delivery_code", length = 50)
  private String deliveryCode;

  @Column(name = "history_modifier_name", length = 5)
  private String historyModifierName;

  @Column(name = "history_modifier_date")
  private LocalDate historyModifierDate;

  public void updateDeliveryCode(String deliveryCode) {
    this.deliveryCode = deliveryCode;
  }

  // ===================== Admin =====================

  // 주문 정보 수정
  public void adminUpdate(String memberFranCode, String memberName, String memberCompanyName,
                          String deliveryAddress, String detailDeliveryAddress,
                          String deliveryCode, String adminName) {
    this.memberFranCode = memberFranCode;
    this.memberName = memberName;
    this.memberCompanyName = memberCompanyName;
    this.deliveryAddress = deliveryAddress;
    this.detailDeliveryAddress = detailDeliveryAddress;
    this.deliveryCode = deliveryCode;
    this.historyModifierName = adminName;
    this.historyModifierDate = LocalDate.now();
  }
}