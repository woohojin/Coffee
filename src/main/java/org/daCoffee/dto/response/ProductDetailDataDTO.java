package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Data;
import org.daCoffee.dto.ProductDTO;

@Data
@Builder
public class ProductDetailDataDTO {
  private int memberTier;
  private int productCount;
  private ProductDTO product;
  private String detailImageName;
}