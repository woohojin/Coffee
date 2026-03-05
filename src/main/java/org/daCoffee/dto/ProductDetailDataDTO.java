package org.daCoffee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailDataDTO {
  private int memberTier;
  private int productCount;
  private ProductDTO product;
  private String detailImageName;
}