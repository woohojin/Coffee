package org.daCoffee.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProductListDataDTO {
  private List<ProductDTO> list;
  private int productCount;
  private int start;
  private int end;
  private int pageInt;
  private String pageType;
  private int memberTier;
}