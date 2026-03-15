package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.daCoffee.entity.Product;

import java.util.List;

@Getter
@Builder
public class ProductListDataDTO {
  private List<Product> list;
  private int productCount;
  private int totalPages;
  private int pageInt;
  private String pageType;
  private int memberTier;
}