package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.daCoffee.dto.ProductDTO;

@Getter
@Builder
public class ProductDetailDataDTO {
  private int memberTier;
  private int productCount;
  private ProductDTO product;
  private String detailImageName;
}