package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.daCoffee.dto.ProductDTO;

import java.util.List;

@Getter
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