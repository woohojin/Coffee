package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.daCoffee.entity.Bean;
import org.daCoffee.entity.Mix;

@Getter
@Builder
public class ProductDetailDataDTO {
  private int memberTier;
  private int productCount;
  private Object product;
  private Bean bean;
  private Mix mix;
  private String detailImageName;
}