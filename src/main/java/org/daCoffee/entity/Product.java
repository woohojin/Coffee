package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import org.daCoffee.dto.request.admin.ProductRequestDTO;

import java.time.LocalDate;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

  @Id
  @Column(name = "product_code", length = 10)
  private String productCode;

  @Column(name = "product_type")
  private Integer productType;

  @Column(name = "product_name", length = 50)
  private String productName;

  @Column(name = "product_price")
  private Integer productPrice;

  @Column(name = "product_unit", length = 20)
  private String productUnit;

  @Column(name = "product_tier", nullable = false)
  private Integer productTier;

  @Column(name = "product_file", length = 30)
  private String productFile;

  @Column(name = "product_sold_out", nullable = false)
  private boolean productSoldOut;

  @Column(name = "product_register_name", nullable = false, length = 5)
  private String productRegisterName;

  @Column(name = "product_register_date", nullable = false)
  private LocalDate productRegisterDate;

  @Column(name = "product_modifier_name", length = 5)
  private String productModifierName;

  @Column(name = "product_modifier_date")
  private LocalDate productModifierDate;

  public void updateSoldOut(boolean soldOut) {
    this.productSoldOut = soldOut;
  }

  public void adminUpdateProduct(ProductRequestDTO dto, String thumbnailFileName, String adminName) {
    this.productCode = dto.getProductCode();
    this.productType = dto.getProductType();
    this.productName = dto.getProductName();
    this.productPrice = dto.getProductPrice();
    this.productUnit = dto.getProductUnit();
    this.productTier = dto.getProductTier();
    this.productFile = thumbnailFileName;
    this.productModifierName = adminName;
    this.productModifierDate = LocalDate.now();
  }
}
