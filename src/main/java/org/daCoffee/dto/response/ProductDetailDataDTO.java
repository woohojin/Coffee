package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailDataDTO {
  private int memberTier;
  private int productCount;
  private Object product;
  private BeanDataDTO bean;
  private MixDataDTO mix;
  private String detailImageName;
}