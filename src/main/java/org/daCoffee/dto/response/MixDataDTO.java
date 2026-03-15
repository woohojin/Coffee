package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MixDataDTO {
  private String mixCompany;
  private String mixUseByDate;
}
