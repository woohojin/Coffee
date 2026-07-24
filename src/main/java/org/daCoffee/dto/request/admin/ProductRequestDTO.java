package org.daCoffee.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
  private String existProductCode;
  private String productCode;
  private int productType;
  private String productName;
  private int productPrice;
  private String productUnit;
  private int productTier;
  private boolean productSoldOut;

  // Bean
  private String beanSpecies;
  private String beanCompany;
  private String beanUseByDate;
  private String beanCountry;

  // Mix
  private String mixCompany;
  private String mixUseByDate;
}